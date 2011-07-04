package de.softwarekollektiv.dbs;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

public class DbConnection {
	public static Logger log = Logger.getLogger(DbConnection.class);

	/*
	 * JDBC Driver and database URL
	 */

	private static String dbUrl = "jdbc:postgresql:dbs_movie";

	/*
	 * Database Credentials
	 */
	private static String user = "kollektiv";
	private static String password = "software";

	static Connection dbConnection = null;

	/*
	 * get Connection to the database
	 */
	synchronized
	public static Connection getConnection() {

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
