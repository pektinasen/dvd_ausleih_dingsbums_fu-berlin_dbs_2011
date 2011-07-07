package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import de.softwarekollektiv.dbs.dbcon.DbConnection;

// TODO unify DirectorsParser & ActorsParser
public class DirectorsParser extends AbstractImdbParser {
	private final DbConnection dbcon;
	
	private PreparedStatement directedByStatement;
	private PreparedStatement directorsStatement;
	private String currentDirector;

	public DirectorsParser(DbConnection dbcon, String file) {
		super(dbcon, file);
		// Name\t\t\tTitles
		super.skipLines = 235;
		super.delimiter = "\t+";
		
		this.dbcon = dbcon;
	}

	@Override
	protected void newLine(String[] lineParts) throws SQLException {
		/*
		 * if newline the current actor has no more featuring movies
		 */
		if (lineParts[0].equals("")) {
			currentDirector = null;
			return;
		}
		
		// TODO HACK
		if(lineParts.length < 2)
			return;

		/*
		 * new Actor starting
		 */
		if (currentDirector == null) {
			currentDirector = lineParts[0];
		}

		String movieTitle = lineParts[1];

		directorsStatement.setString(1, currentDirector);
		directorsStatement.execute();

		ResultSet result = directorsStatement.getGeneratedKeys();
		result.next();
		int dirId = result.getInt(1);

		directedByStatement.setString(1, movieTitle);
		directedByStatement.setInt(2, dirId);
		directedByStatement.execute();
	}

	@Override
	protected void prepareStatements() throws SQLException {
		directorsStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO directors VALUES (DEFAULT, ?)",
				Statement.RETURN_GENERATED_KEYS);
		directedByStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO directedBy VALUES ("
						+ "		(SELECT mov_id FROM movies WHERE title = ?)"
						+ ",?)");
	}

	@Override
	protected void closeStatements() throws SQLException {
		directorsStatement.close();
		directedByStatement.close();
	}

}
