package com.toan.spring.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "Accounts")
public class Account implements Serializable {
	private static final long serialVersionUID  =  -2054386655979281969L;
	
	public static final String ROLE_MANAGER ="MANAGER";
	public static final String ROLE_EMPLOYEE = "EMPLOYEE";
	
	@Id
	@Column(name="User_Name", length = 20, nullable = false)
	private String userName;
	
	@Column(name="Encryted_Password", length = 128, nullable = false)
	private String encrytedPassword;
	
	@Override
	public String toString() {
		return "Account [username=" + this.userName + ", encrytedPassword=" + this.encrytedPassword +  ", userRole=" + this.userRole + "]";
	}
	
	@Column(name="Active", length = 1, nullable = false)
	private boolean active;
	
	@Column(name="User_Role", length = 20, nullable = false)
	private String userRole;
	
	public String getUsername() {
		return userName;
	}

	
	public void setUsername(String username) {
		this.userName = username;
	}

	
	public String getEncrytedPassword() {
		return encrytedPassword;
	}

	
	public void setEncrytedPassword(String encrytedPassword) {
		this.encrytedPassword = encrytedPassword;
	}

	
	public boolean isActive() {
		return active;
	}

	
	public void setActive(boolean active) {
		this.active = active;
	}

	
	public String getUserRole() {
		return userRole;
	}

	
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	
	public static String getRoleManager() {
		return ROLE_MANAGER;
	}

	
	public static String getRoleEmployee() {
		return ROLE_EMPLOYEE;
	}

	
}
