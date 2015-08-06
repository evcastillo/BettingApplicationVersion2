package com.lotus.bettingapplicationver2.enums;

public enum SportCode {
	BASK("BASK"), FOOT("FOOT"), BOXI("BOXI"), TENN("TENN");

	String sportCode;

	SportCode(String sportCode) {
		this.sportCode = sportCode;
	}

	public String getSportCode() {
		return sportCode;
	}
}
