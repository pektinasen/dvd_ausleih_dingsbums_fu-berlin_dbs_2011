package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import de.softwarekollektiv.dbs.dbcon.DbConnection;

// TODO unify DirectorsParser & ActorsParser
public class DirectorsParser extends AbstractImdbParser {
	private final DbConnection dbcon;

	private PreparedStatement movIdStatement;
	private PreparedStatement directorsStatement;
	private PreparedStatement directedByStatement;

	private String currentDirector;
	private int currentDirectorId;

	/*
	 * we need this set, because of some title appears multiple times for one
	 * director, but with different addition
	 */
	Set<String> currentDirectedMovies = new HashSet<String>();

	public DirectorsParser(DbConnection dbcon, String file) {
		super(dbcon, file);
		// Name\t\t\tTitles
		super.skipLines = 235;

		this.dbcon = dbcon;
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
		if (currentDirector == "")

			if (!currentDirectedMovies.contains(movieTitle)) {

				currentDirectedMovies.add(movieTitle);
				movIdStatement.setString(1, movieTitle);
				ResultSet movIdRslt = movIdStatement.executeQuery();

				// only when the movie exists
				if (movIdRslt.next()) {
					int movId = movIdRslt.getInt(1);

					// Only insert the director into the db if we haven't dont
					// so yet, else re-use the key
					if (currentDirectorId < 0) {
						directorsStatement.setString(1, currentDirector);
						directorsStatement.execute();

						ResultSet result = directorsStatement
								.getGeneratedKeys();
						result.next();
						currentDirectorId = result.getInt(1);
					}
					directedByStatement.setInt(1, movId);
					directedByStatement.setInt(2, currentDirectorId);
					directedByStatement.execute();

				}
			}
	}

	@Override
	protected void prepareStatements() throws SQLException {
		movIdStatement = dbcon.getConnection().prepareStatement(
				"SELECT mov_id FROM movies WHERE title = ?");
		directorsStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO directors VALUES (DEFAULT, ?)",
				Statement.RETURN_GENERATED_KEYS);
		directedByStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO directedBy VALUES (?, ?);");
	}

	@Override
	protected void closeStatements() throws SQLException {
		movIdStatement.close();
		directorsStatement.close();
		directedByStatement.close();
	}

	@Override
	protected void executeBatchStatements() throws SQLException {
		// TODO Kein batch processing bis jetzt, da wir den zurückgegebenen
		// key brauchen
	}

}
