package com.toan.spring.entity;

import java.io.Serializable;
import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "Orders", uniqueConstraints = {@UniqueConstraint(columnNames = "Order_Num")})
public class Order implements Serializable {
	private static final long  serialVersionUID = -2576670215015463100L;
	
	@Id
	@Column(name = "ID", length = 50)
	private String id;
	
	@Column(name = "Order_Date", nullable =false)
	private java.util.Date orderDate;
	
	@Column(name = "Order_Num", nullable = false)
	private int orderNum;
	
	@Column(name = "Amount" , nullable = false)
	private double amount;
	
	@Column(name="Customer_Address", length = 255,nullable = false)
	private String customerAddress;
	/**
	 * @return the id
	 */
	public String getId() {
		return id;
	}

	/**
	 * @return the customerAddress
	 */
	public String getCustomerAddress() {
		return customerAddress;
	}

	/**
	 * @param customerAddress the customerAddress to set
	 */
	public void setCustomerAddress(String customerAddress) {
		this.customerAddress = customerAddress;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return the orderDate
	 */
	public java.util.Date getOrderDate() {
		return orderDate;
	}

	/**
	 * @param date the orderDate to set
	 */
	public void setOrderDate(java.util.Date orderDate) {
		this.orderDate = orderDate;
	}

	/**
	 * @return the orderNum
	 */
	public int getOrderNum() {
		return orderNum;
	}

	/**
	 * @param orderNum the orderNum to set
	 */
	public void setOrderNum(int orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the customerName
	 */
	public String getCustomerName() {
		return customerName;
	}

	/**
	 * @param customerName the customerName to set
	 */
	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	/**
	 * @return the customerEmail
	 */
	public String getCustomerEmail() {
		return customerEmail;
	}

	/**
	 * @param customerEmail the customerEmail to set
	 */
	public void setCustomerEmail(String customerEmail) {
		this.customerEmail = customerEmail;
	}

	/**
	 * @return the customerPhone
	 */
	public String getCustomerPhone() {
		return customerPhone;
	}

	/**
	 * @param customerPhone the customerPhone to set
	 */
	public void setCustomerPhone(String customerPhone) {
		this.customerPhone = customerPhone;
	}

	@Column(name = "Customer_Name", length = 255, nullable = false)
	private String customerName;
	
	@Column(name="Customer_Email", length = 120, nullable = false)
	private String customerEmail;
	
	@Column(name ="Customer_Phone", length = 120, nullable = false)
	private String customerPhone;
}
