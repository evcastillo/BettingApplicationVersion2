package com.lotus.bettingapplicationver2.dao;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.lotus.bettingapplicationver2.beans.Customer;
import com.lotus.bettingapplicationver2.beans.Event;
import com.lotus.bettingapplicationver2.beans.Outcome;
import com.lotus.bettingapplicationver2.beans.User;
import com.lotus.bettingapplicationver2.enums.UserType;

public class Main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		AdminDAO adminDAO = AdminOracleDAO.getInstance();
		
		
//		List<Customer> customers = adminDAO.getAllCustomers();
//		
//		System.out.println(customers);
//		String custom = "CUSTOMER";
//		User newUser = new User("ernest", "castillo", new BigDecimal("900"), "admin");
//		
//		System.out.println(newUser.getUserType().getUserTypeCode());
//		adminDAO.createUser(newUser);
		
//		BigDecimal bd = new BigDecimal("1");
//		adminDAO.addBalanceToUser(706, bd);
//		
//		Event event = new Event("EVC15", "BASK", "25/12/2015 12:00:00");
//		System.out.println(event);
//		adminDAO.createEvent(event);
		long aa = 1019;
		List<Outcome> outcomeList = adminDAO.getOutcomesByEventId(1002);
		
		for(Outcome boom: outcomeList ){
			System.out.println(boom.getOutcomeResult().getOutcomeResultCode());
		}
		
	}	

}
