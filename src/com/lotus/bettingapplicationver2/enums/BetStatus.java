package com.lotus.bettingapplicationver2.enums;

public enum BetStatus {
	OPEN("OPEN"), RESULTED("RESULTED"), SETTLED("SETTLED");

	private final String betStatus;

	BetStatus(String betStatus) {
		this.betStatus = betStatus;
	}

	public String getBetStatusCode() {
		return betStatus;
	}

}