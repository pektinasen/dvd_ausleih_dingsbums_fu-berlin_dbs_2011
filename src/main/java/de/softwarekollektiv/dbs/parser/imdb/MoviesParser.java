package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.Parser;

public class MoviesParser extends AbstractImdbParser implements Parser {
	private static final Logger log = Logger.getLogger(MoviesParser.class);
	private final DbConnection dbcon;
	
	private PreparedStatement movieStatement;

	public MoviesParser(DbConnection dbcon, String file) {
		super(dbcon, file);
		super.skipLines = 4;
		this.dbcon = dbcon;
	}

	/*
	 * Each line is independend from any other It consists of one title, release
	 * year and a Category seperated by \t+
	 */
	@Override
	protected void newLine(String[] lineParts) throws SQLException {

		if (lineParts.length < 3)
			// discard entry, if it's too few arguments
			return;
		
		String movieTitle = lineParts[0];
		String movieReleaseString = lineParts[1];

		if (!(movieReleaseString.contains("2010") || movieReleaseString
				.contains("2011"))) {
			return;
		}

		Date movieRelease;
		String movieCategory;
		try {
			//check for entries like '2009-2010'
			if (movieReleaseString.startsWith("201")){
				movieRelease = getFirstDayOfYear(lineParts[1].substring(0, 4));
			}
			else {				
				movieRelease = getFirstDayOfYear(movieReleaseString.split("-")[1]);
			}
			movieCategory = lineParts[2];
			
		} catch (StringIndexOutOfBoundsException e) {
			// it has no year associated
			log.debug(Arrays.toString(lineParts), e);
			movieRelease = getDateFromImdbString(lineParts[0]);
			movieCategory = lineParts[1];
		} catch (NumberFormatException ne) {
			//year is '????'
			log.debug(Arrays.toString(lineParts), ne);
			movieRelease = getDateFromImdbString(lineParts[0]);
			movieCategory = lineParts[2];
		}
		
		if (lineParts.length == 4){
			movieCategory = lineParts[3];
		}

		movieStatement.setString(1, movieTitle);
		movieStatement.setDate(2, movieRelease);
		movieStatement.setString(3, movieCategory);
		
		movieStatement.execute();
	}

	@Override
	protected void prepareStatements() throws SQLException {
		movieStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO movies VALUES (DEFAULT, ?, ?,null,?, null);");
	}

	@Override
	protected void closeStatements() throws SQLException {
		movieStatement.close();
	}
}
