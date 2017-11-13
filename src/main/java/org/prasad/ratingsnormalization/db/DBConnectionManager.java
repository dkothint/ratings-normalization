package org.prasad.ratingsnormalization.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.lang.StringEscapeUtils;
import org.prasad.ratingsnormalization.util.PropsUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DBConnectionManager {

	public static Logger LOGGER = LoggerFactory.getLogger(DBConnectionManager.class);
	private static DBConnectionManager connManager;

	private DBConnectionManager() {
	}

	// Not expecting any concurrent access to this application and hence not
	// burdening it with additional synchronization code.
	public static DBConnectionManager getInstance() {
		if (connManager == null) {
			connManager = new DBConnectionManager();
		}
		return connManager;
	}

	public Connection getConnection() {
		Connection conn = null;
		try {
			PropertiesConfiguration props = PropsUtil.getDBPropsConfiguration();
			String dbname = props.getString("dbname");
			String username = props.getString("username");
			String password = props.getString("password");
			String dbServerAddress = props.getString("dbserveraddr");

			Class.forName("org.postgresql.Driver");
			conn = DriverManager.getConnection(dbServerAddress + dbname, username, password);
		} catch (SQLException e) {
			LOGGER.error("SQL Exception while establishing connection to DB!", e);
		} catch (ClassNotFoundException e) {
			LOGGER.error("JDBC driver class not found!", e);
		} catch (ConfigurationException e) {
			LOGGER.error("DB configuration file retrieval error!", e);
		}
		return conn;
	}

	public void closeConnection(Connection conn) {
		try {
			conn.close();
		} catch (SQLException e) {
			LOGGER.error("SQL Exception while closing connection to DB!", e);
		}
	}

	public void createReviewsTable() {
		Statement stmt = null;
		try {
			Connection conn = getConnection();
			stmt = conn.createStatement();
			String sql = "CREATE TABLE if not exists reviews (ID SERIAL PRIMARY KEY,"
					+ " user_id TEXT NOT NULL, business_id TEXT NOT NULL, stars REAL NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
		} catch (Exception e) {
			LOGGER.error("Error while creating Reviews table!", e);
		}
	}

	public void createBusinessesTable() {
		Statement stmt = null;
		try {
			Connection conn = getConnection();
			stmt = conn.createStatement();
			String sql = "CREATE TABLE if not exists business (ID SERIAL PRIMARY KEY,"
					+ "business_id TEXT NOT NULL, stars REAL NOT NULL, review_count INT NOT NULL, normalized_stars REAL NOT NULL)";
			stmt.executeUpdate(sql);
			stmt.close();
			conn.close();
		} catch (Exception e) {
			LOGGER.error("Error while creating Business table!", e);
		}
	}

	public void insertIntoReviewsTable(ReviewsTuple tuple, Connection conn) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String sql = "INSERT into reviews(user_id, business_id, stars) values('" + tuple.getUserId() + "', '"
					+ tuple.getBusinessId() + "', " + tuple.getRating() + ")";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error("Error while inserting into Reviews table!", e);
		}
	}

	public void insertIntoBusinessTable(BusinessTuple tuple, Connection conn) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String name = tuple.getBusinessName().replace('\'', ' ');
			String sql = "INSERT into business(business_id, stars, review_count, normalized_stars, name) values('" + tuple.getBusinessId() + "', "
					+ tuple.getRating() + ", " + tuple.getReviewsCount() + ", " + tuple.getRating() + ", '" + name + "')";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error("Error while inserting into business table!", e);
		}
	}
	
	public void updateBusinessTable(BusinessTuple tuple, Connection conn) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			String name = tuple.getBusinessName().replace('\'', ' ');
			String sql = "update business set name = '" + name + "' where business_id = '" + tuple.getBusinessId() + "'";
			stmt.executeUpdate(sql);
			stmt.close();
		} catch (SQLException e) {
			LOGGER.error("Error while updating business table!", e);
		}
	}
	
	public ResultSet extractResults(Connection conn, String query) {
		ResultSet rs = null;
        try {
                Statement stmt = conn.createStatement();
                rs = stmt.executeQuery(query);
                
        } catch (SQLException ex) {
        	LOGGER.error("Error while extracting query: "+query, ex);
        }
        return rs;
	}
	
	public void executeUpdate(Connection conn, String query) {
        try {
                Statement stmt = conn.createStatement();
                stmt.executeUpdate(query);
                //conn.commit();
        } catch (SQLException ex) {
        	LOGGER.error("Error while running update query: "+query, ex);
        }
	}
	
	public BusinessTuple getBusinessTuple(Connection conn, String businessId) {
		String query = "select * from business where business_id = '"+businessId + "'";
		ResultSet results = DBConnectionManager.getInstance().extractResults(conn, query);
		BusinessTuple business = null;
		try {
			if(results.next()) {
				business = new BusinessTuple();
				business.setBusinessId(results.getString("business_id"));
				business.setRating(results.getDouble("stars"));
				business.setReviewsCount(results.getInt("review_count"));
			}	
		}
		catch(SQLException e) {
			LOGGER.error("Error while reading business results from DB!", e);
		}
		return business;
	}
}
