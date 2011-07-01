package de.softwarekollektiv.dbs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.softwarekollektiv.dbs.parser.MoviesParser;

public class Main {

	public static Logger log = Logger.getLogger(Main.class);

	

	private static String createScript = "src/main/resources/createScript.sql";

	public static void main(String[] args) throws SQLException,
			ClassNotFoundException, FileNotFoundException {

		

		/*
		 * create a database with scheme
		 */
		log.info("creating database...");

		String query = fileToString(createScript);
		Connection db = Registry.getConnection();
		Statement create = db.createStatement();
		create.execute(query);
		create.close();
		db.commit();

		log.info("begin parsing data");
		
		
		
		MoviesParser mp = new MoviesParser();
		try {
			mp.open("src/main/resources/modmovies.list");
			log.debug("parsing modmovies.list");
			mp.parse();
			
			
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db.close();

	}

	private static String fileToString(String file) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));

			String line = null;
			while ((line = in.readLine()) != null) {
				sb.append(line+"\n");
			}

		} catch (IOException e) {
			// log.warn("Cannot convert file to string", e);
			e.printStackTrace();
		}
		return sb.toString();
	}

}
