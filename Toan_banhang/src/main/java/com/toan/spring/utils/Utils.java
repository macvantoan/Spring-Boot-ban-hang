package com.toan.spring.utils;

import javax.servlet.http.HttpServletRequest;

import com.toan.spring.model.CartInfo;

public class Utils {
		public static CartInfo getCartlnSession(HttpServletRequest request) {
			CartInfo cartInfo =  (CartInfo) request.getSession().getAttribute("myCart");
			if(cartInfo == null) {
				cartInfo = new CartInfo();
				request.getSession().setAttribute("myCart", cartInfo);
			}
			return cartInfo;
		}
		
		public static void removeCartInSession(HttpServletRequest request) {
				request.getSession().removeAttribute("myCart");
		}
		
		public static void storeLastOrderedCartlnSession(HttpServletRequest request, CartInfo cartInfo) {
			request.getSession().setAttribute("lastOrderedCart", cartInfo);
		}
		
		public static CartInfo getLastOrderedCartlnSession(HttpServletRequest request) {
			return (CartInfo) request.getSession().getAttribute("lastOrderedCart");
		}
}
