package com.toan.spring.dao;

import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.toan.spring.entity.Order;
import com.toan.spring.entity.OrderDetail;
import com.toan.spring.entity.Product;
import com.toan.spring.model.CartInfo;
import com.toan.spring.model.CartLineInfo;
import com.toan.spring.model.CustomerInfo;
import com.toan.spring.model.OrderDetaillnfo;
import com.toan.spring.model.OrderInfo;
import com.toan.spring.pagination.PaginationResult;

@Transactional
@Repository
public class OrderDAO {
	@Autowired
	private SessionFactory sessionFactory;

	@Autowired
	private ProductDAO productDAO;

	private int getMaxOrderNum() {
		String sql = "Select max(o.orderNum) from " + Order.class.getName() + " o";
		Session session = this.sessionFactory.getCurrentSession();
		Query<Integer> query = session.createQuery(sql, Integer.class);
		Integer value = (Integer) query.getSingleResult();
		if (value == null) {
			return 0;
		}
		return value;
	}

	@Transactional(rollbackFor = Exception.class)
	public void saveOrder(CartInfo cartInfo) {
		Session session = this.sessionFactory.getCurrentSession();

		int orderNum = this.getMaxOrderNum() + 1;
		Order order = new Order();

		order.setId(UUID.randomUUID().toString());
		order.setOrderNum(orderNum);
		order.setOrderDate(new Date());
		order.setAmount(cartInfo.getAmountTotal());

		CustomerInfo customerInfo = cartInfo.getCustomerInfo();
		order.setCustomerName(customerInfo.getName());
		order.setCustomerEmail(customerInfo.getEmail());
		order.setCustomerPhone(customerInfo.getPhone());
		order.setCustomerAddress(customerInfo.getAddress());

		session.persist(order);

		List<CartLineInfo> lines = cartInfo.getCartLines();

		for (CartLineInfo line : lines) {
			OrderDetail detali = new OrderDetail();
			detali.setId(UUID.randomUUID().toString());
			detali.setOrder(order);
			detali.setAmount(line.getAmount());
			detali.setPrice(line.getProductInfo().getPrice());
			detali.setQuanity(line.getQuantity());

			String code = line.getProductInfo().getCode();
			Product product = this.productDAO.findProduct(code);
			detali.setProduct(product);
			session.persist(detali);
		}
		cartInfo.setOrderNum(orderNum);
		session.flush();
	}

	public PaginationResult<OrderInfo> listOrderInfo(int page, int maxResult, int maxNavigationPage) {
		String sql = "Select new " + OrderInfo.class.getName() 
				+ "(ord.id, ord.orderDate, ord.orderNum, ord.amount,"
				+ "ord.customerName, ord.customerAddress, ord.customerEmail, ord.customerPhone)" + " from "
				+ Order.class.getName() + " ord " + " order by ord.orderNum desc";
		Session session = this.sessionFactory.getCurrentSession();
		Query<OrderInfo> query = session.createQuery(sql, OrderInfo.class);
		return new PaginationResult<OrderInfo>(query, page, maxResult, maxNavigationPage);
	}

	public Order findOrder(String orderId) {
		Session session = this.sessionFactory.getCurrentSession();
		return session.find(Order.class, orderId);
	}

	public OrderInfo getOrderInfo(String orderId) {
		Order order = this.findOrder(orderId);
		if (order == null) {
			return null;
		}
		return new OrderInfo(order.getId(), order.getOrderDate(), order.getOrderNum(), order.getAmount(),
				order.getCustomerName(), order.getCustomerAddress(), order.getCustomerEmail(),
				order.getCustomerPhone());
	}

	public List<OrderDetaillnfo> listOrderDetaillnfos(String orderId) {
		String sql = "Select new " + OrderDetaillnfo.class.getName()
				+ "(d.id, d.product.code, d.product.name , d.quanity,d.price,d.amount)" + " from "
				+ OrderDetail.class.getName() + " d " + " where d.order.id =:orderId";

		Session session = this.sessionFactory.getCurrentSession();
		Query<OrderDetaillnfo> query = session.createQuery(sql, OrderDetaillnfo.class);
		query.setParameter("orderId", orderId);
		return query.getResultList();
	}
}
