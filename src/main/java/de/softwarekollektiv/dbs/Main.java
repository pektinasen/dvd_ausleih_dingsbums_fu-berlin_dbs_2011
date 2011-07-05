package de.softwarekollektiv.dbs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import de.softwarekollektiv.dbs.parser.ActorsParser;
import de.softwarekollektiv.dbs.parser.ImdbParser;
import de.softwarekollektiv.dbs.parser.LocationsParse;
import de.softwarekollektiv.dbs.parser.MoviesParser;

public class Main {

	public static Logger log = Logger.getLogger(Main.class);

	private static String createScript = "src/main/resources/create.sql";

	public static void main(String[] args) throws SQLException,
			ClassNotFoundException, IOException {

		/*
		 * create a database with scheme
		 */
		log.info("creating database...");

		String query = fileToString(createScript);
		Connection db = DbConnection.getConnection();
		Statement create = db.createStatement();
		create.execute(query);
		create.close();
		db.commit();

		log.info("begin parsing data");

		ImdbParser movieParser = new MoviesParser();
		ImdbParser actorsParser = new ActorsParser();
		ImdbParser actressesParser = new ActorsParser();
		ImdbParser locationsParser = new LocationsParse();
		
		
		List<ImdbParser> parsers = new LinkedList<ImdbParser>();
		parsers.add(movieParser);
		parsers.add(actorsParser);
		parsers.add(actressesParser);
		parsers.add(locationsParser);
		
		movieParser.open("src/main/resources/modmovies.list");
		actorsParser.open("src/main/resources/actors.list");
		actressesParser.open("src/main/resources/actresses.list");
		locationsParser.open("src/main/resources/locations.list");
		
		log.debug("parsing lists");
		
		long before = System.currentTimeMillis();	
		
		for (final ImdbParser parser : parsers){

					// TODO Auto-generated method stub
					parser.parse();
					parser.close();
					

		}
		
		log.debug("time: " +( System.currentTimeMillis() - before) +" ms");
		
		db.close();

	}

	private static String fileToString(String file) {
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader in = new BufferedReader(new FileReader(file));

			String line = null;
			while ((line = in.readLine()) != null) {
				sb.append(line + "\n");
			}

		} catch (IOException e) {
			// log.warn("Cannot convert file to string", e);
			e.printStackTrace();
		}
		return sb.toString();
	}

}
