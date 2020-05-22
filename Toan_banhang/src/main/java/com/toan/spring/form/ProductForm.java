package com.toan.spring.form;

import org.springframework.web.multipart.MultipartFile;

import com.toan.spring.entity.Product;

public class ProductForm {
		private String code;
		private String name;
		private double price;
		 
		public ProductForm(Product product) {
			this.code = product.getCode();
			this.name = product.getName();
			this.price = product.getPrice();
		}
		public String getCode() {
			return code;
		}

		/**
		 * @param code the code to set
		 */
		public void setCode(String code) {
			this.code = code;
		}

		/**
		 * @return the name
		 */
		public String getName() {
			return name;
		}

		/**
		 * @param name the name to set
		 */
		public void setName(String name) {
			this.name = name;
		}

		/**
		 * @return the price
		 */
		public double getPrice() {
			return price;
		}

		/**
		 * @param price the price to set
		 */
		public void setPrice(double price) {
			this.price = price;
		}

		/**
		 * @return the newProduct
		 */
		public boolean isNewProduct() {
			return newProduct;
		}

		/**
		 * @param newProduct the newProduct to set
		 */
		public void setNewProduct(boolean newProduct) {
			this.newProduct = newProduct;
		}

		/**
		 * @return the fileData
		 */
		public MultipartFile getFileData() {
			return fileData;
		}

		/**
		 * @param fileData the fileData to set
		 */
		public void setFileData(MultipartFile fileData) {
			this.fileData = fileData;
		}

		private boolean newProduct = false;
		
		private MultipartFile fileData;
		
		public ProductForm() {
			this.newProduct = true;
		}
}
