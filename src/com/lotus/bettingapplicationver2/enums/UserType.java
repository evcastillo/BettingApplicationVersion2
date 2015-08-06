package com.lotus.bettingapplicationver2.enums;

public enum UserType {
	ADMIN("ADMIN"), CUSTOMER("CUSTOMER");

	private final String userType;

	 UserType(String userType) {
		this.userType = userType;
	}

	public String getUserTypeCode() {
		return userType;
	}

}
