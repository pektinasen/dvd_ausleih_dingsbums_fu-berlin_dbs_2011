package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.Parser;

public class LocationsParser extends AbstractImdbParser implements Parser {
	private static final Logger log = Logger.getLogger(LocationsParser.class);
	private final DbConnection dbcon;
	
	private PreparedStatement movieIdStatement;
	private PreparedStatement locationsStatement;
	private PreparedStatement shotInStatement;

	public LocationsParser(DbConnection dbcon, String file) throws SQLException {
		super(dbcon, file);
		super.delimiter = "\t+";
		super.firstStop = "==============";

		this.dbcon = dbcon;
	}

	@Override
	protected void newLine(String[] lineParts) {

		// TODO HACK
		if(lineParts.length < 2)
			return;
		
		String movieTitle = lineParts[0];
		String location = lineParts[1].split("|")[0];
		String[] locationParts = location.split(",");
		String country = locationParts[locationParts.length - 1];
		int movId;
		
		try {
			movieIdStatement.setString(1, movieTitle);
			ResultSet result = movieIdStatement.executeQuery();
			
			// Is movie in database?
			if(result.next()) {
				movId = result.getInt(1);
				result.close();

			
				locationsStatement.setString(1, location);
				locationsStatement.setString(2, country);
				locationsStatement.execute();
	
				result = locationsStatement.getGeneratedKeys();
				result.next();
				int locId = result.getInt(1);
				result.close();
	
				shotInStatement.setInt(1, movId);
				shotInStatement.setInt(2, locId);
				shotInStatement.execute();
			}
		} catch (SQLException e) {
			// TODO eliminate this
			log.debug("SQLException", e);
		}
	}

	@Override
	protected void prepareStatements() throws SQLException {
		movieIdStatement = dbcon.getConnection().prepareStatement(
				"SELECT mov_id FROM movies WHERE title = ?;");
		locationsStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO locations VALUES (DEFAULT, ?, ?)",
				Statement.RETURN_GENERATED_KEYS);
		shotInStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO shotIn VALUES (?, ?);");
	}

	@Override
	protected void closeStatements() throws SQLException {
		movieIdStatement.close();
		locationsStatement.close();
		shotInStatement.close();
	}

}
