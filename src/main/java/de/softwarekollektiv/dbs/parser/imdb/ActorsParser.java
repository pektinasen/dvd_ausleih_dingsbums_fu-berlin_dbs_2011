package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.AbstractParser;
import de.softwarekollektiv.dbs.parser.Parser;

public class ActorsParser extends AbstractParser implements Parser {

	String currentActor;
	private boolean firstLine = true;
	private PreparedStatement actorsStatement;
	private PreparedStatement featuresStatement;
	private PreparedStatement lastActorsSerial;
	
	
	public ActorsParser(DbConnection dbcon, String file, boolean male) throws SQLException {
		super(dbcon, file);
		super.delimiter = "\t+";
		super.firstStop = "----\t\t\t------";
		super.table = "actors";
		super.values = 3;
		
		actorsStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO actors VALUES (DEFAULT, ? , "+male+")");
		featuresStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO features VALUES (?, (" +
				"		SELECT mov_id FROM movies WHERE title = ?)" +
				")");
		
		lastActorsSerial = dbcon.getConnection().prepareStatement(
				"SELECT is_called, last_Value FROM actors_act_id_seq;");
	}

	@Override
	public void newLine(String[] lineParts) {

		/*
		 * if newline the current actor has no more featuring movies
		 */
		if (lineParts[0].equals("")) {
			currentActor = null;
			return;
		}

		/*
		 * new Actor starting
		 */
		if (currentActor == null) {
			currentActor = lineParts[0];
		}
	
		try {
			actorsStatement.setString(1, currentActor);
			actorsStatement.execute();

			ResultSet lastValue = lastActorsSerial.executeQuery();
			lastValue.next();
			int nextActId = 1;
			if (lastValue.getBoolean(1)){
				nextActId = lastValue.getInt(2)+1;
			}
			
			featuresStatement.setInt(1, nextActId);
			featuresStatement.setString(2, lineParts[1].split("  ")[0]);
			featuresStatement.execute();
			
		} catch (SQLException e) {
			log.debug("SQLException", e);
		}

	}
}
