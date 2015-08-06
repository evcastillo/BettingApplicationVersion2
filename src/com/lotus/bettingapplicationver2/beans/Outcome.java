package com.lotus.bettingapplicationver2.beans;

import com.lotus.bettingapplicationver2.enums.BetStatus;
import com.lotus.bettingapplicationver2.enums.OutcomeResult;

public class Outcome {
	private long id;
	private String outcomeDesc;
	private long outcomeEventId;
	private OutcomeResult outcomeResult;

	public Outcome(long id, String outcomeDesc, long outcomeEventId,
			String outcomeResultCode) {
		super();
		this.id = id;
		this.outcomeDesc = outcomeDesc;
		this.outcomeEventId = outcomeEventId;
		this.outcomeResult = OutcomeResult.valueOf(outcomeResultCode.toUpperCase());
	}

	public Outcome(String outcomeDesc, Long outcomeEventId) {
		super();
		this.outcomeDesc = outcomeDesc;
		this.outcomeEventId = outcomeEventId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getOutcomeDesc() {
		return outcomeDesc;
	}

	public void setOutcomeDesc(String outcomeDesc) {
		this.outcomeDesc = outcomeDesc;
	}

	public long getOutcomeEventId() {
		return outcomeEventId;
	}

	public void setOutcomeEventId(long outcomeEventId) {
		this.outcomeEventId = outcomeEventId;
	}

	public OutcomeResult getOutcomeResult() {
		return outcomeResult;
	}

	public void setOutcomeResult(String outcomeResultCode) {
		this.outcomeResult = OutcomeResult.valueOf(outcomeResultCode.toUpperCase());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ (int) (outcomeEventId ^ (outcomeEventId >>> 32));
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Outcome other = (Outcome) obj;
		if (outcomeEventId != other.outcomeEventId)
			return false;
		return true;
	}
	
	
	
}
