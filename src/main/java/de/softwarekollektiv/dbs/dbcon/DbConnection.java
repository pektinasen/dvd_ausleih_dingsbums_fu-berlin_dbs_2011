package de.softwarekollektiv.dbs.dbcon;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DbConnection {
	private static Logger log = Logger.getLogger(DbConnection.class);

	private final String dbUrl = "jdbc:postgresql:dbs_movie";
	private String user = "kollektiv";
	private String password = "software";

	private Connection dbConnection = null;
		
	/**
	 * Set credentials for next try.
	 */
	void setCredentials(String user, String password) {
		this.user = user;
		this.password = password;
	}
	
	/**
	 * Open connection to database.
	 * 
	 * @return true on success, false on failure.
	 */
	public boolean openConnection() {
		if(dbConnection != null)
			// Should not happen
			closeConnection();
		
		log.info("Connecting to database...");
		try {
			dbConnection = DriverManager.getConnection(dbUrl, user, password);
			dbConnection.setAutoCommit(false);
		} catch (SQLException e) {
			log.warn("Could not establish database connection. ", e);
			dbConnection = null;
			return false;
		}
		
		return true;
	}
	
	/**
	 * Close connection to database.
	 */
	public void closeConnection() {
		if (dbConnection != null) {
			try {
				dbConnection.close();
			} catch (SQLException e) {
				log.warn("Could not close database connection", e);
			}
		}
	}
	
	/**
	 * Get Connection to the database
	 */
	public Connection getConnection() {
		return dbConnection;
	}
}
