package com.lotus.bettingapplicationver2.beans;

import java.math.BigDecimal;

import com.lotus.bettingapplicationver2.enums.UserType;

public class Customer extends User {

	public Customer(long id, String username, String password,
			BigDecimal balance, String userType) {
		super(id, username, password, balance, userType);
		// TODO Auto-generated constructor stub
	}

	public Customer(String username, String password, BigDecimal balance,
			String userType) {
		super(username, password, balance, userType);
		// TODO Auto-generated constructor stub
	}

}
