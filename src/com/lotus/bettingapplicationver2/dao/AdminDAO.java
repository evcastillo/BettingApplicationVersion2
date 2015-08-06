package com.lotus.bettingapplicationver2.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.lotus.bettingapplicationver2.beans.Bet;
import com.lotus.bettingapplicationver2.beans.Customer;
import com.lotus.bettingapplicationver2.beans.Event;
import com.lotus.bettingapplicationver2.beans.Outcome;
import com.lotus.bettingapplicationver2.beans.User;
import com.lotus.bettingapplicationver2.enums.OutcomeResult;
import com.lotus.bettingapplicationver2.enums.SportCode;
import com.lotus.bettingapplicationver2.enums.UserType;

public interface AdminDAO {
	
	void createUser(User newUser);
	
	void addBalanceToUser(long userId, BigDecimal addBalance);

	void createEvent(Event newEvent);

	void createOutcome(Outcome newOutcome);
	
	void resultOutcome(long outcomeId, String outcomeResult);

	User getUserByUsername(String username);

	Event getEventByEventCode(String eventCode);

	List<Outcome> getOutcomesByEventCode(String eventCode);
	
	List<Bet> getAllBets();

	List<Customer> getAllCustomers();
	
	List<Event> getAllEvents();

	List<Outcome> getAllOutcomes();

	List<Outcome> getOutcomesByEventId(long aa);
	
	

	

	

}
