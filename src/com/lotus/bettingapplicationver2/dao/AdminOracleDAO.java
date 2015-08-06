package com.lotus.bettingapplicationver2.dao;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.lotus.bettingapplicationver2.beans.Bet;
import com.lotus.bettingapplicationver2.beans.Customer;
import com.lotus.bettingapplicationver2.beans.Event;
import com.lotus.bettingapplicationver2.beans.Outcome;
import com.lotus.bettingapplicationver2.beans.User;
import com.lotus.bettingapplicationver2.enums.BetStatus;
import com.lotus.bettingapplicationver2.enums.OutcomeResult;
import com.lotus.bettingapplicationver2.enums.SportCode;
import com.lotus.bettingapplicationver2.enums.UserType;

public class AdminOracleDAO implements AdminDAO {

	private static final AdminOracleDAO INSTANCE = new AdminOracleDAO();

	public static AdminOracleDAO getInstance() {
		return INSTANCE;
	}

	private AdminOracleDAO() {
		try {
			Class.forName("oracle.jdbc.driver.OracleDriver");
		} catch (ClassNotFoundException e) {
			throw new RuntimeException("Database connection failed.");
		}
	}

	private static Connection getConnection() throws SQLException {
		Connection connection = DriverManager.getConnection(
				"jdbc:oracle:thin:@localhost:1522:xe", "ernest", "password");
		connection.setAutoCommit(false);
		return connection;
	}

	public void createUser(User newUser) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			preparedStatement = connection
					.prepareStatement("INSERT INTO users (user_id, username, password, balance, user_type) values (users_seq.nextVal, ?, ?, ?, ?)");
			preparedStatement.setString(1, newUser.getUsername());
			preparedStatement.setString(2, newUser.getPassword());
			preparedStatement.setBigDecimal(3, newUser.getBalance());
			preparedStatement.setString(4, newUser.getUserType()
					.getUserTypeCode());
			preparedStatement.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();

			}
			e.printStackTrace();
			throw new RuntimeException("Database error.");

		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}

	}

	public void addBalanceToUser(long userId, BigDecimal addBalance) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		Statement getCurrentBalanceStatement = null;
		try {
			connection = getConnection();
			String getBalanceById = "SELECT balance from users where user_id = "
					+ userId;
			getCurrentBalanceStatement = connection.createStatement();
			ResultSet resultSetCurrentBalance = getCurrentBalanceStatement
					.executeQuery(getBalanceById);

			if (resultSetCurrentBalance.next()) {
				BigDecimal currentBalance = extractBalanceFromResultSet(resultSetCurrentBalance);
				BigDecimal newBalance = currentBalance.add(addBalance);
				preparedStatement = connection
						.prepareStatement("UPDATE users SET balance = ? where user_id = ?");
				preparedStatement.setBigDecimal(1, newBalance);
				preparedStatement.setLong(2, userId);
				preparedStatement.executeUpdate();
				connection.commit();
			}

		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();

			}
			e.printStackTrace();
			throw new RuntimeException("Database error.");
		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}
	}

	public void createEvent(Event newEvent) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			preparedStatement = connection
					.prepareStatement("INSERT INTO events (event_id, event_code, event_sport_code, event_date) values (events_seq.nextVal, ?, ?, TO_DATE('"
							+ newEvent.getEvent_startDate()
							+ "','DD/MM/YYYY hh24:mi:ss'))");
			preparedStatement.setString(1, newEvent.getEvent_code());
			preparedStatement.setString(2, newEvent.getEvent_sportCode()
					.getSportCode());
			preparedStatement.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();

			}
			e.printStackTrace();
			throw new RuntimeException("Database error.");

		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}

	}

	public void createOutcome(Outcome newOutcome) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		try {
			connection = getConnection();
			preparedStatement = connection
					.prepareStatement("insert into outcomes (outcome_id, outcome_desc, event_id) values (outcomes_seq.nextVal, ?, ?)");
			preparedStatement.setString(1, newOutcome.getOutcomeDesc());
			preparedStatement.setLong(2, newOutcome.getOutcomeEventId());
			preparedStatement.executeUpdate();
			connection.commit();

		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();

			}
			e.printStackTrace();
			throw new RuntimeException("Database error.");

		} finally {
			if (preparedStatement != null) {
				try {
					preparedStatement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}
	}

	@Override
	public void resultOutcome(long outcomeId, String outcomeResult) {
		List<Outcome> outcomes = getAllOutcomes();

		Connection connection = null;
		Statement statement = null;
		try {
			connection = getConnection();
			String SQLUpdateOutcomeResult = "UPDATE outcomes SET outcome_result = '"
					+ outcomeResult.toUpperCase()
					+ "' WHERE outcome_id = "
					+ outcomeId;
			statement = connection.createStatement();
//			updateEventBetSatus(outcomeId);
			for (Outcome outcome : outcomes) {
				if (outcome.getId() == outcomeId) {
					outcome.setOutcomeResult(outcomeResult);
				}
			}
			statement.executeUpdate(SQLUpdateOutcomeResult);
			connection.commit();
		} catch (SQLException e) {
			try {
				if (connection != null) {
					connection.rollback();
				}
			} catch (SQLException e1) {
				e1.printStackTrace();

			}
			e.printStackTrace();
			throw new RuntimeException("Database error.");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}

	}

	public User getUserByUsername(String username) {
		Connection connection = null;
		PreparedStatement statement = null;
		User user = null;
		try {
			connection = getConnection();
			statement = connection
					.prepareStatement("SELECT * FROM users WHERE username =  '"
							+ username + "'");
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				user = extractUserFromResultSet(resultSet);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error.");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}
		return user;

	}

	public Event getEventByEventCode(String eventCode) {
		Connection connection = null;
		PreparedStatement statement = null;
		Event event = null;
		try {
			connection = getConnection();
			statement = connection
					.prepareStatement("SELECT * FROM events WHERE event_code =  '"
							+ eventCode + "'");
			ResultSet resultSet = statement.executeQuery();
			if (resultSet.next()) {
				event = extractEventFromResultSet(resultSet);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error.");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}
		return event;
	}

	public List<Outcome> getOutcomesByEventId(long eventId) {
		Connection connection = null;
		PreparedStatement statement = null;
		List<Outcome> outcomes = new ArrayList<Outcome>();
		try {
			connection = getConnection();
			statement = connection
					.prepareStatement("SELECT * FROM outcomes WHERE event_id = "
							+ eventId);
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				outcomes.add(extractOutcomeFromResultSet(resultSet));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error.");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}
		return outcomes;

	}

	public List<Outcome> getOutcomesByEventCode(String eventCode) {
		Connection connection = null;
		PreparedStatement statement = null;
		List<Outcome> outcomes = new ArrayList<Outcome>();
		try {
			connection = getConnection();
			statement = connection
					.prepareStatement("SELECT * FROM outcomes o, events e WHERE e.event_code = '"
							+ eventCode + "' and e.event_id = o.event_id");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				outcomes.add(extractOutcomeFromResultSet(resultSet));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error.");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}
		return outcomes;

	}

	public List<Bet> getAllBets() {
		Connection connection = null;
		PreparedStatement statement = null;
		List<Bet> bets = new ArrayList<Bet>();
		try {
			connection = getConnection();
			statement = connection.prepareStatement("SELECT * FROM bets");
			ResultSet resultSet = statement.executeQuery();
			while (resultSet.next()) {
				bets.add(extractBetFromResultSet(resultSet));
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error.");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}
		return bets;
	}

	public List<Customer> getAllCustomers() {
		Connection connection = null;
		Statement statement = null;
		List<Customer> customers = new ArrayList<Customer>();

		try {
			connection = getConnection();
			statement = connection.createStatement();
			String sqlQuery = "SELECT * FROM users WHERE user_type = 'CUSTOMER'";
			ResultSet resultSet = statement.executeQuery(sqlQuery);
			while (resultSet.next()) {
				customers.add(extractCustomerFromResultSet(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error.");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}
		return customers;
	}

	public static List<User> getAllUsers() {

		Connection connection = null;
		Statement statement = null;
		List<User> users = new ArrayList<User>();

		try {
			connection = getConnection();
			statement = connection.createStatement();
			String sqlQuery = "SELECT * FROM users";
			ResultSet resultSet = statement.executeQuery(sqlQuery);
			while (resultSet.next()) {
				users.add(extractUserFromResultSet(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error.");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}
		return users;
	}

	public List<Event> getAllEvents() {
		Connection connection = null;
		Statement statement = null;
		List<Event> events = new ArrayList<Event>();
		try {
			connection = getConnection();
			String sqlQuery = "SELECT * FROM events";
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlQuery);
			while (resultSet.next()) {
				events.add(extractEventFromResultSet(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error.");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}
		return events;
	}

	public List<Outcome> getAllOutcomes() {
		Connection connection = null;
		Statement statement = null;
		List<Outcome> outcomes = new ArrayList<Outcome>();
		try {
			connection = getConnection();
			String sqlQuery = "SELECT * FROM outcomes";
			statement = connection.createStatement();
			ResultSet resultSet = statement.executeQuery(sqlQuery);
			while (resultSet.next()) {
				outcomes.add(extractOutcomeFromResultSet(resultSet));
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error.");
		} finally {
			if (statement != null) {
				try {
					statement.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close statement");
				}
			}
			if (connection != null) {
				try {
					connection.close();
				} catch (SQLException e) {
					throw new RuntimeException("Unable to close connection");
				}
			}
		}
		return outcomes;
	}

	private Customer extractCustomerFromResultSet(ResultSet resultSet)
			throws SQLException {
		long id = resultSet.getLong("user_id");
		String username = resultSet.getString("username");
		String password = resultSet.getString("password");
		BigDecimal balance = resultSet.getBigDecimal("balance");
		String userType = resultSet.getString("user_type");
		Customer customer = new Customer(id, username, password, balance,
				userType);

		return customer;

	}

	private static User extractUserFromResultSet(ResultSet resultSet)
			throws SQLException {
		long id = resultSet.getLong("user_id");
		String username = resultSet.getString("username");
		String password = resultSet.getString("password");
		BigDecimal balance = resultSet.getBigDecimal("balance");
		String userType = resultSet.getString("user_type");
		User user = new User(id, username, password, balance, userType);

		return user;

	}

	private static BigDecimal extractBalanceFromResultSet(ResultSet resultSet)
			throws SQLException {

		BigDecimal balance = resultSet.getBigDecimal("balance");

		return balance;

	}

	private static Event extractEventFromResultSet(ResultSet resultSet)
			throws SQLException {
		long id = resultSet.getLong("event_id");
		String eventCode = resultSet.getString("event_code");
		String eventSportCode = resultSet.getString("event_sport_code");
		Timestamp eventStartDate = resultSet.getTimestamp("event_date");
		String eventBetStatus = resultSet.getString("event_bet_status");

		Event event = new Event(id, eventCode, eventSportCode, eventStartDate,
				eventBetStatus);

		return event;

	}

	private static Outcome extractOutcomeFromResultSet(ResultSet resultSet)
			throws SQLException {
		long id = resultSet.getLong("outcome_id");
		String outcomeDesc = resultSet.getString("outcome_desc");
		long outcomeEventId = resultSet.getLong("event_id");
		String outcomeResult = resultSet.getString("outcome_result");
		Outcome outcome = new Outcome(id, outcomeDesc, outcomeEventId,
				outcomeResult);

		return outcome;

	}

	private static Bet extractBetFromResultSet(ResultSet resultSet)
			throws SQLException {
		long id = resultSet.getLong("bet_id");
		long betEventId = resultSet.getLong("event_id");
		long betUserId = resultSet.getLong("user_id");
		BigDecimal betAmount = resultSet.getBigDecimal("bet_amount");
		long betOutcomeId = resultSet.getLong("outcome_id");
		String betIsSettled = resultSet.getString("bet_issettled");
		Bet bet = new Bet(id, betEventId, betUserId, betAmount, betOutcomeId,
				betIsSettled);
		return bet;
	}

	private static long getEventIdFromResultSet(ResultSet resultSet)
			throws SQLException {
		long eventId = resultSet.getLong("event_id");
		return eventId;
	}

//	private void updateEventBetSatus(long outcomeId) {
//		Statement getEventIdStatement = null;
//		Statement updateEventBetStatusStatement = null;
//		Connection connection = null;
//
//		long eventId = 0;
//		try {
//			connection = getConnection();
//			getEventIdStatement = connection.createStatement();
//			updateEventBetStatusStatement = connection.createStatement();
//			String SQLGetEventId = "SELECT event_id FROM outcomes WHERE outcome_id = "
//					+ outcomeId;
//			ResultSet rs = getEventIdStatement.executeQuery(SQLGetEventId);
//			while (rs.next()) {
//				eventId = getEventIdFromResultSet(rs);
//			}
//			String SQLUpdateEventResult = "update events set event_bet_status = 'RESULTED' where event_id = "
//					+ eventId;
//			List<Outcome> selectedOutcomes = getOutcomesByEventId(outcomeId);
//			if (selectedOutcomes.get(0).equals(selectedOutcomes.get(1)) && !selectedOutcomes.get(0).getOutcomeResult().getOutcomeResultCode().equalsIgnoreCase("NONE")) {
//				updateEventBetStatusStatement
//						.executeQuery(SQLUpdateEventResult);
//				connection.commit();
//			}else{
//				return;
//			}
//
//		} catch (SQLException e) {
//		}
//
//	}
}
