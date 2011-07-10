package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.softwarekollektiv.dbs.dbcon.DbConnection;

// TODO unify DirectorsParser & ActorsParser
public class DirectorsParser extends AbstractImdbParser {
	private final DbConnection dbcon;
	private final Map<String, Integer> movIdCache;
	
	private PreparedStatement directorsStatement;
	private PreparedStatement directedByStatement;

	private String currentDirector = null;
	private int currentDirectorId = -1;
	private int nextId = 1;

	/*
	 * we need this set, because of some title appears multiple times for one
	 * director, but with different addition
	 */
	Set<String> currentDirectedMovies = new HashSet<String>();

	public DirectorsParser(DbConnection dbcon, String file, Map<String, Integer> movIdCache) {
		super(dbcon, file);
		// Name\t\t\tTitles
		super.skipLines = 235;

		this.dbcon = dbcon;
		this.movIdCache = movIdCache;
	}

	protected void newLine(String[] lineParts) throws SQLException {

		// if newline the current actor has no more featuring movies
		// Hier lag der Hund begraben
		// Zeilen wie "\t\t\t<titel>" die noch zu einem Actor gehören
		// haben auch "" als lineParts[0]
		if (lineParts.length == 1) {
			currentDirectedMovies = new HashSet<String>();
			currentDirector = null;
			currentDirectorId = -1;
			return;
		}

		// new Actor starting
		if (currentDirector == null) {
			currentDirector = lineParts[0];
		}

		// Extract movie title and look up in database
		String movieTitle = lineParts[1].split("  ")[0];
		// TODO this is wrong. (equals, Logik?)
		if (currentDirector == "")

			if (!currentDirectedMovies.contains(movieTitle)) {

				currentDirectedMovies.add(movieTitle);
				Integer movId = movIdCache.get(movieTitle);

				// only when the movie exists
				if (movId != null) {
					
					// Only insert the director into the db if we haven't dont
					// so yet, else re-use the key
					if (currentDirectorId < 0) {
						currentDirectorId = nextId++;
						directorsStatement.setInt(1, currentDirectorId);
						directorsStatement.setString(2, currentDirector);
						directorsStatement.addBatch();
					}
					directedByStatement.setInt(1, movId);
					directedByStatement.setInt(2, currentDirectorId);
					directedByStatement.addBatch();

				}
			}
	}

	@Override
	protected void prepareStatements() throws SQLException {
		directorsStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO directors VALUES (DEFAULT, ?)");
		directedByStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO directedBy VALUES (?, ?);");
	}

	@Override
	protected void closeStatements() throws SQLException {
		directorsStatement.close();
		directedByStatement.close();
	}

	@Override
	protected void executeBatchStatements() throws SQLException {
		directorsStatement.executeBatch();
		directedByStatement.executeBatch();
	}

}
