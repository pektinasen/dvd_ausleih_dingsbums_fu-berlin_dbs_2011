package de.softwarekollektiv.dbs.parser;

import java.sql.Connection;
import java.sql.Statement;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.app.Utils;
import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.imdb.ActorsParser;
import de.softwarekollektiv.dbs.parser.imdb.ActressesParser;
import de.softwarekollektiv.dbs.parser.imdb.AdditionalMoviesParser;
import de.softwarekollektiv.dbs.parser.imdb.DirectorsParser;
import de.softwarekollektiv.dbs.parser.imdb.LocationsParser;
import de.softwarekollektiv.dbs.parser.imdb.MoviesParser;
import de.softwarekollektiv.dbs.parser.imdb.ReleaseDateParser;
import de.softwarekollektiv.dbs.parser.misc.CustomerParser;
import de.softwarekollektiv.dbs.parser.misc.OscarsParser;
import de.softwarekollektiv.dbs.parser.misc.RentalsParser;

// TODO rename again
public class ParserCommander implements MenuItem {
	private static final Logger log = Logger.getLogger(ParserCommander.class);
	private final DbConnection dbcon;

	public ParserCommander(DbConnection dbcon) {
		this.dbcon = dbcon;
	}

	@Override
	public String getTitle() {
		return "Import data";
	}

	@Override
	public String getDescription() {
		return "Create a new database and import data from the given files.";
	}

	@Override
	public boolean run() throws Exception {
		// Create a database with scheme
		log.info("Creating database...");
		String query = Utils.fileToString("src/main/resources/sql/create.sql");
		Connection db = dbcon.getConnection();
		Statement create = db.createStatement();
		create.execute(query);
		create.close();
		db.commit();

		Map<String, Integer> movIdCache = new HashMap<String, Integer>();
		
		List<Parser> parsers = new LinkedList<Parser>();
		parsers.add(new MoviesParser(dbcon,
				"src/main/resources/modmovies.list", movIdCache));
		parsers.add(new AdditionalMoviesParser(dbcon,
				"src/main/resources/addmovies.list", movIdCache));
		parsers.add(new ReleaseDateParser(dbcon, "src/main/resources/release-dates.list", movIdCache));
		parsers.add(new ActorsParser(dbcon,
				"src/main/resources/actors.list", movIdCache));
		parsers.add(new ActressesParser(dbcon,
				"src/main/resources/actresses.list", movIdCache));
		parsers.add(new LocationsParser(dbcon, "src/main/resources/locations.list", movIdCache));
		parsers.add(new DirectorsParser(dbcon, "src/main/resources/directors.list", movIdCache));
		parsers.add(new CustomerParser(dbcon,
				"src/main/resources/customers.list"));
		parsers.add(new RentalsParser(dbcon,
				"src/main/resources/rentals.list", movIdCache));
		parsers.add(new OscarsParser(dbcon,
				"src/main/resources/oscars.list", movIdCache));
		
		long before = System.currentTimeMillis();
		long beforeParser = 0;
		for (final Parser parser : parsers) {	
			beforeParser = System.currentTimeMillis();
			log.info("Running " + parser.getClass().getSimpleName() + "...");
			parser.parse();
			log.debug("Time: " + (System.currentTimeMillis() - beforeParser) + " ms");
		}
		log.debug("Total time: " + (System.currentTimeMillis() - before) + " ms");
		
		return true;
	}
}
