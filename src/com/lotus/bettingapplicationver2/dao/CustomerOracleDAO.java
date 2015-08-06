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
import java.util.List;

import com.lotus.bettingapplicationver2.beans.Bet;
import com.lotus.bettingapplicationver2.beans.Event;
import com.lotus.bettingapplicationver2.beans.Outcome;
import com.lotus.bettingapplicationver2.enums.SportCode;

public class CustomerOracleDAO implements CustomerDAO {

	private static final CustomerOracleDAO INSTANCE = new CustomerOracleDAO();

	public static CustomerOracleDAO getInstance() {
		return INSTANCE;
	}

	private CustomerOracleDAO() {
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

	@Override
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

	@Override
	public List<Event> getEventsBySportCode(String sportCode) {
		Connection connection = null;
		Statement statement = null;
		List<Event> events = new ArrayList<Event>();
		try {
			connection = getConnection();
			String sqlQuery = "SELECT * FROM events WHERE event_sport_code = '"+sportCode+"'";
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

	public List<Bet> getAllBetsByCustomer(long id) {
		Connection connection = null;
		PreparedStatement statement = null;
		List<Bet> bets = new ArrayList<Bet>();
		try {
			connection = getConnection();
			statement = connection
					.prepareStatement("SELECT * FROM bets WHERE user_id = ?");
			statement.setLong(1, id);
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

	@Override
	public void createBet(Bet newBet) {
		Connection connection = null;
		PreparedStatement preparedStatement = null;
		PreparedStatement PreparedStatementUpdateBalance = null;
		try {
			connection = getConnection();
			preparedStatement = connection
					.prepareStatement("INSERT INTO bets (bet_id, bet_amount, user_id, event_id, outcome_id) values (bets_seq.nextVal, ?, ?, ?, ?)");
			preparedStatement.setBigDecimal(1, newBet.getBetAmount());
			preparedStatement.setLong(2, newBet.getBetUserId());
			preparedStatement.setLong(3, newBet.getBetEventId());
			preparedStatement.setLong(4, newBet.getBetOutcomeId());
			BigDecimal currentBalance = getCurrentBalanceOfLoggedUser(newBet
					.getBetUserId());

			BigDecimal betValue = newBet.getBetAmount();
			BigDecimal remainingBalance = currentBalance.subtract(betValue);
			PreparedStatementUpdateBalance = connection
					.prepareStatement("UPDATE users SET balance = ? WHERE user_id = ?");
			PreparedStatementUpdateBalance.setBigDecimal(1, remainingBalance);
			PreparedStatementUpdateBalance.setLong(2, newBet.getBetUserId());

			PreparedStatementUpdateBalance.executeUpdate();
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
			if (preparedStatement != null
					|| PreparedStatementUpdateBalance != null) {
				try {
					preparedStatement.close();
					PreparedStatementUpdateBalance.close();
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
	public BigDecimal getCurrentBalanceOfLoggedUser(long id) {
		Connection connection = null;
		Statement getCurrentBalanceStatement = null;
		BigDecimal currentBalance = null;
		try {
			connection = getConnection();
			String getBalanceByUsername = "SELECT * FROM users WHERE user_id ="
					+ id;
			getCurrentBalanceStatement = connection.createStatement();
			ResultSet resultSetCurrentBalance = getCurrentBalanceStatement
					.executeQuery(getBalanceByUsername);
			while (resultSetCurrentBalance.next()) {
				currentBalance = extractBalanceFromResultSet(resultSetCurrentBalance);
				connection.commit();
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException("Database error.");
		} finally {
			if (getCurrentBalanceStatement != null) {
				try {
					getCurrentBalanceStatement.close();
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
		return currentBalance;

	}

	private static BigDecimal extractBalanceFromResultSet(ResultSet resultSet)
			throws SQLException {

		BigDecimal balance = resultSet.getBigDecimal("balance");

		return balance;

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

}
