package com.lotus.bettingapplicationver2.beans;

import java.math.BigDecimal;
import java.util.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import com.lotus.bettingapplicationver2.enums.BetStatus;
import com.lotus.bettingapplicationver2.enums.SportCode;

public class Event {
	private SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:SS");
	private long id;
	private String eventCode;
	private SportCode eventSportCode;
	private Timestamp eventStartDate;
	private BetStatus eventBetStatus;

	public Event(long id, String eventCode, String eventSportCode,
			Timestamp eventStartDate, String eventBetStatus) {
		super();
		this.id = id;
		this.eventCode = eventCode;
		this.eventSportCode = SportCode.valueOf(eventSportCode);
		this.eventStartDate = eventStartDate;
		this.eventBetStatus = BetStatus.valueOf(eventBetStatus);
	}

	public Event(String eventCode, String eventSportCode, String eventDate) {
		super();
		this.eventCode = eventCode;
		this.eventSportCode = SportCode.valueOf(eventSportCode);
		try {
			Date tempEventDate = (Date) df.parse(eventDate);
			this.eventStartDate = new Timestamp(tempEventDate.getTime());
		} catch (ParseException e) {
			e.printStackTrace();
		}

	}

	public long getEvent_id() {
		return id;
	}

	public void setEvent_id(long event_id) {
		this.id = event_id;
	}

	public String getEvent_code() {
		return eventCode;
	}

	public void setEvent_code(String event_code) {
		this.eventCode = event_code;
	}

	public SportCode getEvent_sportCode() {
		return eventSportCode;
	}

	public void setEvent_sportCode(String event_sportCode) {
		this.eventSportCode = SportCode.valueOf(event_sportCode);
	}

	public String getEvent_startDate() {

		return df.format(eventStartDate);
	}

	public void setEvent_startDate(Timestamp event_startDate) {
		this.eventStartDate = event_startDate;
	}

	public BetStatus getEvent_betStatus() {
		return eventBetStatus;
	}

	public void setEvent_betStatus(BetStatus event_betStatus) {
		this.eventBetStatus = event_betStatus;
	}

	@Override
	public String toString() {
		return "Event [eventStartDate=" + eventStartDate + "]";
	}

}
