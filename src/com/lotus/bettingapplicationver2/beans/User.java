package com.lotus.bettingapplicationver2.beans;

import java.math.BigDecimal;

import org.codehaus.jackson.annotate.JsonIgnore;

import com.lotus.bettingapplicationver2.enums.UserType;

public class User {
	private long id;
	private String username;
	private String password;
	private BigDecimal balance;
	private UserType userType;

	public User(String username, String password, BigDecimal balance,
			String userType) {
		super();
		this.username = username;
		this.password = password;
		this.balance = balance;
		this.userType = UserType.valueOf(userType.toUpperCase());
	}
	
	public User(long id, String username, String password, BigDecimal balance,
			String userType) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.balance = balance;
		this.userType = UserType.valueOf(userType.toUpperCase());
	}
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	@JsonIgnore
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public String toString() {
		return "USER_ID: " + id + " USERNAME: " + username + " BALANCE: "
				+ balance + " USER_TYPE: " + userType.getUserTypeCode();
	}
}
