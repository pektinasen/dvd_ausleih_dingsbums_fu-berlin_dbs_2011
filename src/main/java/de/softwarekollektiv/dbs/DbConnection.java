package de.softwarekollektiv.dbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DbConnection {
	private static Logger log = Logger.getLogger(DbConnection.class);

	private String dbUrl = "jdbc:postgresql:dbs_movie";
	private String user = "kollektiv";
	private String password = "software";

	private Connection dbConnection = null;
	
	public void setCredentials(String user, String password) {
		this.user = user;
		this.password = password;
	}
	
	/**
	 * Get Connection to the database
	 * 
	 * @return the java.sql.Connection object on success, null on failure
	 */
	synchronized
	public Connection getConnection() {

		if (dbConnection == null) {
			log.info("Connecting to Database");
			try {
				dbConnection = DriverManager.getConnection(dbUrl, user, password);
			} catch (SQLException e) {
				log.warn("wäh, wäh; cannot create db connection", e);
			}
		}
		return dbConnection;
	}
}
