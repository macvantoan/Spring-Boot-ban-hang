package com.toan.spring.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.toan.spring.dao.OrderDAO;
import com.toan.spring.dao.ProductDAO;
import com.toan.spring.entity.Product;
import com.toan.spring.form.CustomerForm;
import com.toan.spring.model.CartInfo;
import com.toan.spring.model.CustomerInfo;
import com.toan.spring.model.ProductInfo;
import com.toan.spring.pagination.PaginationResult;
import com.toan.spring.utils.Utils;
import com.toan.spring.validator.CustomerFormValidator;

@Controller
@Transactional
public class MainController {
	@Autowired
	private OrderDAO orderDAO;

	@Autowired
	private ProductDAO productDAO;

	@Autowired
	private CustomerFormValidator customerFormValidator;

	@InitBinder
	public void myInitBinder(WebDataBinder dataBinder) {
		Object target = dataBinder.getTarget();
		if (target == null) {
			return;
		}
		System.out.print("Target=" + target);
		if (target.getClass() == CartInfo.class) {

		} else if (target.getClass() == CustomerForm.class) {
			dataBinder.setValidator(customerFormValidator);
		}
	}

	@RequestMapping("/403")
	public String accessDenied() {
		return "/403";
	}

	@RequestMapping("/")
	public String home() {
		return "index";
	}

	@RequestMapping("/productList")
	public String listProductHandler(Model model, @RequestParam(value = "name", defaultValue = "") String likeName,
			@RequestParam(value = "page", defaultValue = "1") int page) {
		final int maxResult = 3;
		final int maxNavigationPage = 10;

		PaginationResult<ProductInfo> result = productDAO.queryProducts(page, maxResult, maxNavigationPage, likeName);
		model.addAttribute("paginationProducts", result);
		return "productList";
	}

	@RequestMapping("/buyProduct")
	public String listProductHandler(HttpServletRequest request, Model model,
			@RequestParam(value = "code", defaultValue = "") String code) {
		Product product = null;
		if (code != null && code.length() > 0) {
			product = productDAO.findProduct(code);
		}
		if (product != null) {
			CartInfo cartInfo = Utils.getCartlnSession(request);
			ProductInfo productInfo = new ProductInfo(product);
			cartInfo.addProduct(productInfo, 1);
		}
		return "redirect:/shoppingCart";
	}

	@RequestMapping("/shoppingCartRemoveProduct")
	public String removeProductHandler(HttpServletRequest request, Model model,
			@RequestParam(value = "code", defaultValue = "") String code) {
		Product product = null;
		if (code != null && code.length() > 0) {
			product = productDAO.findProduct(code);
		}
		if (product != null) {
			CartInfo cartInfo = Utils.getCartlnSession(request);
			ProductInfo productInfo = new ProductInfo(product);
			cartInfo.removeProduct(productInfo);
		}
		return "redirect:/shoppingCart";
	}

	@RequestMapping(value = {"/shoppingCart"}, method = RequestMethod.POST)
	public String shoppingCartUpdateQty(HttpServletRequest request, Model model,
			@ModelAttribute("cartForm") CartInfo cartForm) {
		CartInfo cartInfo = Utils.getCartlnSession(request);
		cartForm.updateQuantity(cartForm);
		return "redirect:/shoppingCart";
	}

	@RequestMapping(value = { "/shoppingCart" }, method = RequestMethod.GET)
	public String shoppingCartHandler(HttpServletRequest request, Model model) {
		CartInfo myCart = Utils.getCartlnSession(request);
		model.addAttribute("cartForm", myCart);
		return "shoppingCart";
	}

	@RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.GET)
	public String shoppingCartCustomerForm(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getCartlnSession(request);
		if (cartInfo.isEmpty()) {
			return "redirect:/shoppingCart";
		}
		CustomerInfo customerInfo = cartInfo.getCustomerInfo();

		CustomerForm customerForm = new CustomerForm(customerInfo);

		model.addAttribute("customerForm", customerForm);
		return "shoppingCartCustomer";
	}

	@RequestMapping(value = { "/shoppingCartCustomer" }, method = RequestMethod.POST)
	public String shoppingCartCustomerSave(HttpServletRequest request, Model model,
			@ModelAttribute("customerForm") @Validated CustomerForm customerForm, BindingResult result,
			final RedirectAttributes redirectAttributes) {
		if (result.hasErrors()) {
			customerForm.setValid(false);
			return "shoppingCartCustomer";
		}
		customerForm.setValid(true);
		CartInfo cartInfo = Utils.getCartlnSession(request);
		CustomerInfo customerInfo = new CustomerInfo(customerForm);
		cartInfo.setCustomerInfo(customerInfo);
		return "redirect:/shoppingCartConfirmation";
	}

	@RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.GET)
	public String shoppingCartConfirmationReview(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getCartlnSession(request);
		if (cartInfo == null || cartInfo.isEmpty()) {
			return "redirect:/shoppingCart";
		} else if (!cartInfo.isValidCustome()) {
			return "redirect:/";
		}
		model.addAttribute("myCart", cartInfo);
		return "shoppingCartConfirmation";
	}

	@RequestMapping(value = { "/shoppingCartConfirmation" }, method = RequestMethod.POST)
	public String shoppingCartConfirmationSave(HttpServletRequest request, Model model) {
		CartInfo cartInfo = Utils.getCartlnSession(request);
		if (cartInfo.isEmpty()) {
			return "redirect:/shoppingCart";
		} else if (!cartInfo.isValidCustome()) {
			return "redirect:/shoppingCartCustomer";
		}
		try {
			orderDAO.saveOrder(cartInfo);
		} catch (Exception e) {
			return "shoppingCartConfirmation";
		}
		Utils.removeCartInSession(request);
		Utils.storeLastOrderedCartlnSession(request, cartInfo);
		return "redirect:/shoppingCartFinalize";
	}

	@RequestMapping(value = "/shoppingCartFinalize", method = RequestMethod.GET)
	public String shoppingCartFinalize(HttpServletRequest request, Model model) {
		CartInfo lastOrderedCart = Utils.getLastOrderedCartlnSession(request);
		if (lastOrderedCart == null) {
			return "redirect:/shoppingCart";
		}
		model.addAttribute("lastOrderedCart", lastOrderedCart);
		return "shoppingCartFinalize";
	}

	@RequestMapping(value = "/productImage", method = RequestMethod.GET)
	public void productImage(HttpServletRequest request, HttpServletResponse reponse, Model model,
			@RequestParam("code") String code)throws IOException {
		Product product = null;
		if(code != null) {
			product = this.productDAO.findProduct(code);
		}
		if(product != null && product.getImage() != null) {
			reponse.setContentType("image/jpeg, image/jpg, image/png, image/gif");
			reponse.getOutputStream().write(product.getImage());
		}
		reponse.getOutputStream().close();
	}

}
