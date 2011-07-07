package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.Parser;

public class MoviesParser extends AbstractImdbParser implements Parser {
	
	private final DbConnection dbcon;
	
	private PreparedStatement movStmt;

	private static final Pattern yearPattern = Pattern.compile("(\\d{4})?-?((\\d{4})|\\?{4})");
	
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

		String movieTitle = lineParts[0];
		String movieReleaseString = null;
		String movieCategory = null;
		
		// normal case
		if (lineParts.length == 3){
			movieReleaseString = parseReleaseString(lineParts[1]);
			movieCategory = lineParts[2];
		}
		// the release year field is missing
		else if (lineParts.length == 2){
			if (lineParts[1].equals("A") || lineParts[1].equals("B")){
				/*
				 * if it has no year, it's put in the db anyway.
				 */
				movieReleaseString = "2010";
				movieCategory = lineParts[1];
			}
			else {
				movieReleaseString = parseReleaseString(lineParts[1]);
			}
		}
		else if (lineParts.length == 4) {
			movieReleaseString = parseReleaseString(lineParts[1]);
			movieCategory = lineParts[3];
		}
		
		/*
		 * if the date is still less than 2010, then discard this entry
		 */
		if (Integer.parseInt(movieReleaseString) < 2010){
			log.debug(Arrays.toString(lineParts));
			return;
		}
		
		Date movieRelease = getFirstDayOfYear(movieReleaseString);	
		
		movStmt.setString(1, movieTitle);
		movStmt.setDate(2, movieRelease);
		movStmt.setString(3, movieCategory);
		
		movStmt.execute();

	}

	private String parseReleaseString(String input) {
		
		Matcher m = yearPattern.matcher(input);
		m.find();
		String localRelease;
		if (m.groupCount() >= 2){
			String group2 = m.group(2);
			if (group2.equals("????")){
				localRelease = "2011";
			}
			else 
				localRelease = group2;
		}
		else {
			localRelease = m.group(1);
		}
		return localRelease;
	}

	@Override
	protected void prepareStatements() throws SQLException {
		movStmt = dbcon.getConnection().prepareStatement(
				"INSERT INTO movies VALUES (DEFAULT, ?, ?,null,?, null);");
	}

	@Override
	protected void closeStatements() throws SQLException {
		movStmt.close();
	}
}
