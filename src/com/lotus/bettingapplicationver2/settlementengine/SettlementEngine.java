package com.lotus.bettingapplicationver2.settlementengine;

import java.util.ArrayList;
import java.util.List;

import com.lotus.bettingapplicationver2.beans.Bet;
import com.lotus.bettingapplicationver2.beans.Event;
import com.lotus.bettingapplicationver2.beans.User;
import com.lotus.bettingapplicationver2.dao.AdminDAO;
import com.lotus.bettingapplicationver2.dao.AdminOracleDAO;

public class SettlementEngine implements Runnable {
	private AdminDAO adminDAO = AdminOracleDAO.getInstance();
	List<Event> allEvents = adminDAO.getAllEvents();
	List<Bet> allBets = adminDAO.getAllBets();
	List<User> allUsers = AdminOracleDAO.getAllUsers();

	@Override
	public void run() {
		for (Event event : allEvents) {
			if (event.getEvent_betStatus().getBetStatusCode()
					.equalsIgnoreCase("RESULTED")) {
				confirmViaBet(event.getEvent_id(), event.getEvent_betStatus().getBetStatusCode());
			}
		}
	}

	private void confirmViaBet(long eventId, String betStatusCode) {
		for(Bet bet: allBets){
			if(bet.getBetEventId() == eventId){
//				confirmViaUsers()
			}
		}

	}

}
