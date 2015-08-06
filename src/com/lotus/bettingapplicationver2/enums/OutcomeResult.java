package com.lotus.bettingapplicationver2.enums;

public enum OutcomeResult {
	WIN("WIN"), LOSE("LOSE"), DRAW("DRAW"), NONE("NONE");

	String outcomeResultCode;

	OutcomeResult(String outcomeResultCode) {
		this.outcomeResultCode = outcomeResultCode;
	}

	public String getOutcomeResultCode() {
		return outcomeResultCode;
	}
}
