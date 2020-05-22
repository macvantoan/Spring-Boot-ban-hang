package com.toan.spring.dao;

import java.io.IOException;
import java.sql.Date;

import javax.persistence.NoResultException;


import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.toan.spring.entity.Product;
import com.toan.spring.form.ProductForm;
import com.toan.spring.model.ProductInfo;
import com.toan.spring.pagination.PaginationResult;

@Transactional
@Repository
public class ProductDAO {
	@Autowired
	private SessionFactory sessionFactory;

	public Product findProduct(String code) {
		try {

			String sql = "Select e from " + Product.class.getName() + " e Where e.code =:code";
			Session session = this.sessionFactory.getCurrentSession();
			Query<Product> query = session.createQuery(sql, Product.class);
			query.setParameter("code", code);
			
			return (Product) query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	public ProductInfo findProductInfo(String code) {
		Product product = this.findProduct(code);
		if (product == null) {
			return null;
		}
		return new ProductInfo(product.getCode(), product.getName(), product.getPrice());
	}

	@Transactional(propagation = Propagation.REQUIRES_NEW, rollbackFor = Exception.class)
	public void save(ProductForm productform) {
		Session session = this.sessionFactory.getCurrentSession();
		String code = productform.getCode();

		Product product = null;

		boolean isNew = false;
		if (code != null) {
			product = this.findProduct(code);
		}
		if (product == null) {
			isNew = true;
			product = new Product();
			product.setCreateDate(new Date(0));
		}
		product.setCode(code);
		product.setName(productform.getName());
		product.setPrice(productform.getPrice());

		if (productform.getFileData() != null) {
			byte[] image = null;
			try {
				image = productform.getFileData().getBytes();
			} catch (IOException e) {

			}
			if (image != null && image.length > 0) {
				product.setImage(image);
			}
		}
		if (isNew) {
			session.persist(product);
		}
		session.flush();
	}

	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage,
			String likeName) {
		String sql = "Select new " + ProductInfo.class.getName() 
                + "(p.code, p.name, p.price) " + " from "
                + Product.class.getName() + " p ";
        if (likeName != null && likeName.length() > 0) {
            sql += " Where lower(p.name) like :likeName ";
        }
        sql += " order by p.createDate desc ";
    
        Session session = this.sessionFactory.getCurrentSession();
        Query<ProductInfo> query = session.createQuery(sql, ProductInfo.class);
       
       
        if (likeName != null && likeName.length() > 0) {
            query.setParameter("likeName", "%" + likeName.toLowerCase() + "%");
        }
        System.out.print(query);
        return new PaginationResult<ProductInfo>(query, page, maxResult, maxNavigationPage);
	}
	public PaginationResult<ProductInfo> queryProducts(int page, int maxResult, int maxNavigationPage){
		return queryProducts(page, maxResult, maxNavigationPage,null);
	}
	

}
