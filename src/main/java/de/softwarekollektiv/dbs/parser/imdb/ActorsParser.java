package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.Parser;

public class ActorsParser extends AbstractImdbParser implements Parser {
	private final DbConnection dbcon;
	private final boolean male;
	
	private PreparedStatement movIdStatement;
	private PreparedStatement actorsStatement;
	private PreparedStatement featuresStatement;
	
	private String currentActor;
	private int currentActorId;

	public ActorsParser(DbConnection dbcon, String file, boolean male) {
		super(dbcon, file);
		super.delimiter = "\t+";
		super.firstStop = "----\t\t\t------";
		super.table = "actors";
		super.values = 3;
		
		this.dbcon = dbcon;
		this.male = male;
	}

	protected void newLine(String[] lineParts) throws SQLException {

		// if newline the current actor has no more featuring movies
		if (lineParts[0].equals("")) {
			currentActor = null;
			currentActorId = -1;
			return;
		}
		
		// TODO Some lines are broken. This is ugly.
		if(lineParts.length < 2)
			return;
		
		// new Actor starting
		if (currentActor == null) {
			currentActor = lineParts[0];				
		}

		// Extract movie title and look up in database
		String movieTitle = lineParts[1].split("  ")[0];
		movIdStatement.setString(1, movieTitle);
		ResultSet movIdRslt = movIdStatement.executeQuery();
		
		// only when the movie exists
		if(movIdRslt.next()) {
			int movId = movIdRslt.getInt(1);
				
			// Only insert the actor/actress into the db if we haven't dont so yet, else re-use the key
			if (currentActorId < 0){
				actorsStatement.setString(1, currentActor);
				actorsStatement.execute();
				
				ResultSet result = actorsStatement.getGeneratedKeys();
				result.next();
				currentActorId = result.getInt(1);
			}
			
			featuresStatement.setInt(1, movId);
			featuresStatement.setInt(2, currentActorId);
			featuresStatement.execute();
		}
	}

	@Override
	protected void prepareStatements() throws SQLException {
		movIdStatement = dbcon.getConnection().prepareStatement(
				"SELECT mov_id FROM movies WHERE title = ?");
		actorsStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO actors VALUES (DEFAULT, ? , " + male + ")",
				Statement.RETURN_GENERATED_KEYS);
		featuresStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO features VALUES (?, ?);");
	}

	@Override
	protected void closeStatements() throws SQLException {
		movIdStatement.close();
		actorsStatement.close();
		featuresStatement.close();
	}
}
