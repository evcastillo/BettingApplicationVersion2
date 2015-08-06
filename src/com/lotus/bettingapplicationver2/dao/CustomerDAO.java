package com.lotus.bettingapplicationver2.dao;

import java.math.BigDecimal;
import java.util.List;

import com.lotus.bettingapplicationver2.beans.Bet;
import com.lotus.bettingapplicationver2.beans.Event;
import com.lotus.bettingapplicationver2.enums.SportCode;

public interface CustomerDAO {

	List<Event> getAllEvents(); 
	
	List<Event> getEventsBySportCode(String sportCode);
	
	List<Bet> getAllBetsByCustomer(long id);
	
	void createBet(Bet bet);
	
	BigDecimal getCurrentBalanceOfLoggedUser(long id);
}
