package com.toan.spring.model;

import com.toan.spring.entity.Product;

public class ProductInfo {
	private String code;
	private String name;
	private double price;
	public ProductInfo() {
		
	}
	public ProductInfo(Product product) {
		this.code = product.getCode();
		this.name = product.getName();
		this.price = product.getPrice();
	}
	
	public String getCode() {
		return code;
	}
	public ProductInfo(String code, String name, double price) {
		
		this.code = code;
		this.name = name;
		this.price = price;
	}
	
	public void setCode(String code) {
		this.code = code;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public double getPrice() {
		return price;
	}
	
	public void setPrice(double price) {
		this.price = price;
	}
	
	
	
}