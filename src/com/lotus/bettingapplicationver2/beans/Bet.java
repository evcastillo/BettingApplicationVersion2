package com.lotus.bettingapplicationver2.beans;

import java.math.BigDecimal;

public class Bet {
	private long id;
	private long betEventId;
	private long betUserId;
	private BigDecimal betAmount;
	private long betOutcomeId;
	private boolean betIsSettled = false;

	public Bet(long id, long betEventId, long betCustomerId,
			BigDecimal betAmount, long betOutcomeId, String betIsSettled) {
		super();
		this.id = id;
		this.betEventId = betEventId;
		this.betUserId = betCustomerId;
		this.betAmount = betAmount;
		this.betOutcomeId = betOutcomeId;
		this.betIsSettled = Boolean.parseBoolean(betIsSettled);
	}

	
	
	
	public Bet(long betEventId, long betUserId, BigDecimal betAmount,
			long betOutcomeId) {
		super();
		this.betEventId = betEventId;
		this.betUserId = betUserId;
		this.betAmount = betAmount;
		this.betOutcomeId = betOutcomeId;
	}




	public long getId() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getBetEventId() {
		return this.betEventId;
	}

	public void setBetEventId(long betEventId) {
		this.betEventId = betEventId;
	}

	public long getBetUserId() {
		return this.betUserId;
	}

	public void setBetUserId(long betCustomerId) {
		this.betUserId = betCustomerId;
	}

	public BigDecimal getBetAmount() {
		return this.betAmount;
	}

	public void setBetAmount(BigDecimal betAmount) {
		this.betAmount = betAmount;
	}

	public long getBetOutcomeId() {
		return this.betOutcomeId;
	}

	public void setOutcomeBetId(long outcomeBetId) {
		this.betOutcomeId = outcomeBetId;
	}

	public Boolean getBetIsSettled() {
		return this.betIsSettled;
	}

	public void betIsSettled(String betIsSettled) {
		this.betIsSettled = Boolean.parseBoolean(betIsSettled);
	}
}
