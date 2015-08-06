package com.lotus.bettingapplicationver2.restapi;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.json.JSONObject;

import com.lotus.bettingapplicationver2.beans.Bet;
import com.lotus.bettingapplicationver2.beans.Event;
import com.lotus.bettingapplicationver2.beans.Outcome;
import com.lotus.bettingapplicationver2.beans.User;
import com.lotus.bettingapplicationver2.dao.AdminDAO;
import com.lotus.bettingapplicationver2.dao.AdminOracleDAO;
import com.lotus.bettingapplicationver2.dao.CustomerDAO;
import com.lotus.bettingapplicationver2.dao.CustomerOracleDAO;

@Path("")
public class BettingApplicationRestApi {

	private AdminDAO adminDAO = AdminOracleDAO.getInstance();
	private CustomerDAO customerDAO = CustomerOracleDAO.getInstance();
	private List<User> users = AdminOracleDAO.getAllUsers();


	private static User loggedUser;

	@Path("/login")
	@POST
	@Produces("application/json")
	public Response login(@FormParam("username") String username,
			@FormParam("password") String password)
			throws JsonGenerationException, JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (username == null || password == null) {
			jsonObject.put("Bad Request",
					"'username and password parameter is required.'");
			return Response.status(400).entity(jsonObject.toString()).build();
		} else {
			for (User user : users) {
				if (user.getUsername().equals(username)
						&& user.getPassword().equals(password)) {
					loggedUser = user;
					jsonObject.put("success", true);
					return Response.status(200).entity(jsonObject.toString())
							.build();
				}
			}
		}
		loggedUser = null;
		jsonObject.put("success", false);
		return Response.status(401).entity(jsonObject.toString()).build();

	}

	@Path("/admin")
	@GET
	@Produces("application/json")
	public Response confirmAdmin() throws JSONException,
			JsonGenerationException, JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (notAdmin()) {
			jsonObject.put("Forbidden", "you must log as an admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		}
		jsonObject.put("Success", true);
		return Response.status(200).entity(jsonObject.toString()).build();

	}

	@Path("/admin/user")
	@GET
	@Produces("application/json")
	public Response showUsersList() throws JsonGenerationException,
			JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (notAdmin()) {
			jsonObject.put("Forbidden", "you must log as an admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else {
			String response = "{}";
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.writeValueAsString(users);
			return Response.status(200).entity(response).build();
		}
	}

	@Path("/admin/user/{username}")
	@GET
	@Produces("application/json")
	public Response showUser(@PathParam("username") String username)
			throws JsonGenerationException, JsonMappingException, IOException {
		String response = "{}";
		JSONObject jsonObject = new JSONObject();
		ObjectMapper objectMapper = new ObjectMapper();
		if (notAdmin()) {
			jsonObject.put("Forbidden", "you must log as an admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else {
			User user = adminDAO.getUserByUsername(username);
			response = objectMapper.writeValueAsString(user);
			return Response.status(200).entity(response).build();
		}
	}

	@Path("/admin/user")
	@POST
	@Produces("application/json")
	public Response createUser(@FormParam("username") String username,
			@FormParam("password") String password,
			@FormParam("balance") BigDecimal balance,
			@FormParam("userType") String userType)
			throws JsonGenerationException, JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (notAdmin()) {
			jsonObject.put("Forbidden", "you must log as an admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else if (username == null || password == null || balance == null
				|| userType == null) {
			jsonObject
					.put("Bad Request",
							"'username', 'password', 'balance', 'userType' parameter is required.");
			return Response.status(400).entity(jsonObject.toString()).build();
		}
		try {
			User newUser = new User(username, password, balance, userType);
			adminDAO.createUser(newUser);
			jsonObject.put("success", true);
			return Response.status(200).entity(jsonObject.toString()).build();
		} catch (Exception e) {
			jsonObject.put("success", "false");
			return Response.status(401).entity(jsonObject.toString()).build();
		}

	}

	@Path("/admin/user/addBalance")
	@POST
	@Produces("application/json")
	public Response Addbalance(@FormParam("userId") long userId,
			@FormParam("addBalance") BigDecimal addBalance) {
		JSONObject jsonObject = new JSONObject();
		if (notAdmin()) {
			jsonObject.put("Forbidden", "you must log as an admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else {
			adminDAO.addBalanceToUser(userId, addBalance);
			jsonObject.put("success", true);
			return Response.status(200).entity(jsonObject.toString()).build();
		}
	}

	@Path("/admin/event")
	@GET
	@Produces("application/json")
	public Response showEventsList() throws JsonGenerationException,
			JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (notAdmin()) {
			jsonObject.put("Forbidden", "you must log as an admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else {
			List<Event> events = adminDAO.getAllEvents();
			String response = "{}";
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.writeValueAsString(events);
			return Response.status(200).entity(response).build();
		}
	}

	@Path("/admin/event/{eventCode}")
	@GET
	@Produces("application/json")
	public Response showEvent(@PathParam("eventCode") String eventCode)
			throws JsonGenerationException, JsonMappingException, IOException {
		String response = "{}";
		JSONObject jsonObject = new JSONObject();
		ObjectMapper objectMapper = new ObjectMapper();
		if (notAdmin()) {
			jsonObject.put("Forbidden", "you must log as an admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else {
			Event event = adminDAO.getEventByEventCode(eventCode);
			response = objectMapper.writeValueAsString(event);
			return Response.status(200).entity(response).build();
		}
	}

	@Path("/admin/event")
	@POST
	@Produces("application/json")
	public Response createUser(@FormParam("eventCode") String eventCode,
			@FormParam("eventSportCode") String eventSportCode,
			@FormParam("eventDate") String eventDate)
			throws JsonGenerationException, JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (notAdmin()) {
			jsonObject.put("Forbidden", "you must log as an admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else if (eventCode == null || eventSportCode == null
				|| eventDate == null) {
			jsonObject.put("Bad Request",
					"'eventCode', 'eventSportCode', 'eventDate'");
			return Response.status(400).entity(jsonObject.toString()).build();
		}
		try {
			Event newEvent = new Event(eventCode, eventSportCode, eventDate);
			adminDAO.createEvent(newEvent);
			jsonObject.put("success", true);
			return Response.status(200).entity(jsonObject.toString()).build();
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("success", "false");
			return Response.status(401).entity(jsonObject.toString()).build();
		}

	}

	@Path("/admin/outcome")
	@GET
	@Produces("application/json")
	public Response showOutcomeList() throws JsonGenerationException,
			JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (notAdmin()) {
			jsonObject.put("Forbidden", "you must log as an admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else {
			List<Outcome> outcomes = adminDAO.getAllOutcomes();
			String response = "{}";
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.writeValueAsString(outcomes);
			return Response.status(200).entity(response).build();
		}
	}

	@Path("/admin/outcome/{eventCode}")
	@GET
	@Produces("application/json")
	public Response showOutcomeByEventCode(
			@PathParam("eventCode") String eventCode)
			throws JsonGenerationException, JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (notAdmin()) {
			jsonObject.put("Forbidden", "you must log as an admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else {
			List<Outcome> outcomes = adminDAO.getOutcomesByEventCode(eventCode);
			String response = "{}";
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.writeValueAsString(outcomes);
			return Response.status(200).entity(response).build();
		}
	}

	@Path("/admin/outcome")
	@POST
	@Produces("application/json")
	public Response createOutcome(@FormParam("outcomeDesc") String outcomeDesc,
			@FormParam("outcomeEventId") long outcomeEventId)
			throws JsonGenerationException, JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (notAdmin()) {
			jsonObject.put("Forbidden", "you must log as an admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else if (outcomeDesc == null || outcomeEventId == 0) {
			jsonObject
					.put("Bad Request",
							"'outcomeDesc' and 'outcomeEventId' parameters are required");
			return Response.status(400).entity(jsonObject.toString()).build();
		}
		try {
			Outcome newOutcome = new Outcome(outcomeDesc, outcomeEventId);
			adminDAO.createOutcome(newOutcome);
			jsonObject.put("success", true);
			return Response.status(200).entity(jsonObject.toString()).build();
		} catch (Exception e) {
			jsonObject.put("success", "false");
			return Response.status(401).entity(jsonObject.toString()).build();
		}

	}

	@Path("/admin/outcome/result")
	@POST
	@Produces("application/json")
	public Response resultOutcome(@FormParam("outcomeId") long outcomeId,
			@FormParam("outcomeResult") String outcomeResult)
			throws JsonGenerationException, JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (notAdmin()) {
			jsonObject.put("Forbidden", "you must log as an admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else if (outcomeResult == null || outcomeId == 0) {
			jsonObject.put("Bad Request",
					"'outcomeId' and 'outcomeResult' parameters are required");
			return Response.status(400).entity(jsonObject.toString()).build();
		}
		try {
			adminDAO.resultOutcome(outcomeId, outcomeResult);
			jsonObject.put("success", true);
			return Response.status(200).entity(jsonObject.toString()).build();
		} catch (Exception e) {
			e.printStackTrace();
			jsonObject.put("success", "false");
			return Response.status(401).entity(jsonObject.toString()).build();
		}

	}

	@Path("/admin/bet")
	@GET
	@Produces("application/json")
	public Response showBetList() throws JsonGenerationException,
			JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (notAdmin()) {
			jsonObject.put("Forbidden", "you must log as an admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else {
			List<Bet> bets = adminDAO.getAllBets();
			String response = "{}";
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.writeValueAsString(bets);
			return Response.status(200).entity(response).build();
		}
	}

	@Path("/customer")
	@GET
	@Produces("application/json")
	public Response confirmCustomer() throws JSONException,
			JsonGenerationException, JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (loggedUser == null) {
			jsonObject.put("Forbidden",
					"you must log in as a customer or admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		}
		jsonObject.put("Success", true);
		return Response.status(200).entity(jsonObject.toString()).build();

	}

	@Path("/customer/event")
	@GET
	@Produces("application/json")
	public Response showEventsListFromCustomer()
			throws JsonGenerationException, JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (loggedUser == null) {
			jsonObject.put("Forbidden",
					"you must log in as a customer or admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else {
			List<Event> events = customerDAO.getAllEvents();
			String response = "{}";
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.writeValueAsString(events);
			return Response.status(200).entity(response).build();
		}
	}

	@Path("/customer/event/{sportCode}")
	@GET
	@Produces("application/json")
	public Response showEventsListBySportCode(
			@PathParam("sportCode") String sportCode)
			throws JsonGenerationException, JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (loggedUser == null) {
			jsonObject.put("Forbidden",
					"you must log in as a customer or admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else {
			List<Event> events = customerDAO.getEventsBySportCode(sportCode);
			String response = "{}";
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.writeValueAsString(events);
			return Response.status(200).entity(response).build();
		}
	}

	@Path("/customer/bet")
	@GET
	@Produces("application/json")
	public Response showBetListByLoggedCustomer()
			throws JsonGenerationException, JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (loggedUser == null) {
			jsonObject.put("Forbidden",
					"you must log in as a customer or admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else {
			List<Bet> bets = customerDAO.getAllBetsByCustomer(loggedUser
					.getId());
			String response = "{}";
			ObjectMapper objectMapper = new ObjectMapper();
			response = objectMapper.writeValueAsString(bets);
			return Response.status(200).entity(response).build();
		}
	}

	@Path("/customer/bet")
	@POST
	@Produces("application/json")
	public Response betOnEvent(@FormParam("eventId") long eventId,
			@FormParam("outcomeId") long outcomeId,
			@FormParam("betAmount") BigDecimal betAmount)
			throws JsonGenerationException, JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (loggedUser == null) {
			jsonObject.put("Forbidden",
					"you must log in as a customer or admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		} else if (eventId == 0 || outcomeId == 0 || betAmount == null) {
			jsonObject
					.put("Bad Request",
							"'eventId', 'outcomeId' and 'betAmount' parameters are required");
			return Response.status(400).entity(jsonObject.toString()).build();
		} else if (betAmount.compareTo(loggedUser.getBalance()) > 0) {
			BigDecimal currentBalance = customerDAO
					.getCurrentBalanceOfLoggedUser(loggedUser.getId());
			jsonObject.put("success", false + ", you only have "
					+ currentBalance);
			return Response.status(400).entity(jsonObject.toString()).build();
		}

		try {
			Bet newBet = new Bet(eventId, loggedUser.getId(), betAmount,
					outcomeId);
			customerDAO.createBet(newBet);
			jsonObject.put("success", true);
			return Response.status(200).entity(jsonObject.toString()).build();
		} catch (Exception e) {
			jsonObject.put("success", "false");
			return Response.status(401).entity(jsonObject.toString()).build();
		}

	}

	@Path("/customer/balance")
	@GET
	@Produces("application/json")
	public Response showCurrentBalanceOfLoggedUser() throws JSONException,
			JsonGenerationException, JsonMappingException, IOException {
		JSONObject jsonObject = new JSONObject();
		if (loggedUser == null) {
			jsonObject.put("Forbidden",
					"you must log in as a customer or admin");
			return Response.status(403).entity(jsonObject.toString()).build();
		}
		BigDecimal currentBalance = customerDAO
				.getCurrentBalanceOfLoggedUser(loggedUser.getId());
		jsonObject.put("CurrentBalance", currentBalance);
		return Response.status(200).entity(jsonObject.toString()).build();

	}

	private boolean notAdmin() {
		return loggedUser == null
				|| !loggedUser.getUserType().getUserTypeCode().equals("ADMIN");
	}

}
