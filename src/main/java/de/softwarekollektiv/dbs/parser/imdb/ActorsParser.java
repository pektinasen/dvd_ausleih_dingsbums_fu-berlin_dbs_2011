package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Map;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.Parser;

public class ActorsParser extends AbstractImdbParser implements Parser {
	private final DbConnection dbcon;
	private final Map<String, Integer> movIdCache;
	
	protected boolean male;
	private PreparedStatement actorsStatement;
	private PreparedStatement featuresStatement;
	
	private String currentActor;
	private int currentActorId = -1;
	
	// This is static so that Actresses (who share
	// a table with Actors in the db) get their own
	// range of ids
	private static int nextId = 1;	

	public ActorsParser(DbConnection dbcon, String file, Map<String, Integer> movIdCache) {
		super(dbcon, file);
		super.skipLines = 239;
		super.stopAfter = 11240132;
		this.dbcon = dbcon;
		this.male = true;
		this.movIdCache = movIdCache;
	}

	protected void newLine(String[] lineParts) throws SQLException {
		// if newline the current actor has no more featuring movies
		// Hier lag der Hund begraben
		// Zeilen wie "\t\t\t<titel>" die noch zu einem Actor gehören
		// haben auch "" als lineParts[0]
		if (lineParts.length == 1) {
			currentActor = null;
			currentActorId = -1;
			return;
		}
		
		// new Actor starting
		if (currentActor == null) {
			currentActor = lineParts[0];				
		}

		// Extract movie title and look up in database
		String movieTitle = lineParts[1].split("  ")[0];
		Integer movId = movIdCache.get(movieTitle);
		
		// only when the movie exists
		if(movId != null) {
				
			// Only insert the actor/actress into the db if we haven't dont so yet, else re-use the key
			if (currentActorId < 0) {
				currentActorId = nextId++;
				actorsStatement.setInt(1, currentActorId);
				actorsStatement.setString(2, currentActor);
				actorsStatement.addBatch();			
			}
			
			featuresStatement.setInt(1, movId);
			featuresStatement.setInt(2, currentActorId);
			featuresStatement.addBatch();
		}
	}

	@Override
	protected void prepareStatements() throws SQLException {
		actorsStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO actors VALUES (?, ? , " + male + ")");
		featuresStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO features VALUES (?, ?);");
	}

	@Override
	protected void closeStatements() throws SQLException {
		actorsStatement.close();
		featuresStatement.close();
	}

	@Override
	protected void executeBatchStatements() throws SQLException {
		actorsStatement.executeBatch();
		featuresStatement.executeBatch();
	}
}
