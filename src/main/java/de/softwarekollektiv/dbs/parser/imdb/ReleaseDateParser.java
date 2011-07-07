package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.Parser;

/*
 * TODO es werden nur korrekt formatierte Datumse zugelassen
 * <Tag:int> <Month:String> <year:int>
 */
public class ReleaseDateParser extends AbstractImdbParser implements Parser {
	private static final Logger log = Logger.getLogger(ReleaseDateParser.class);
	private final DbConnection dbcon;
	
	private PreparedStatement updateDateStatement;

	public ReleaseDateParser(DbConnection dbcon, String file) {
		super(dbcon, file);
		super.skipLines = 14;
		
		this.dbcon = dbcon;
	}

	@Override
	protected void newLine(String[] lineParts) throws SQLException {

		if (!(lineParts[0].contains("2010") || lineParts[0].contains("2011"))) {
			return;
		}

		if (!lineParts[1].contains("US")) {
			return;
		}

		String dateString = lineParts[1].split(":")[1];
		String region = lineParts[1].split(":")[0];
		String[] dateParts = dateString.split(" ");
		if (dateParts.length == 3) {

			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(dateParts[0]));
			java.util.Date date = null;
			try {
				date = new SimpleDateFormat("MMM", Locale.ENGLISH)
						.parse(dateParts[1].substring(0, 3));
			} catch (ParseException e) {
				log.debug("Could not parse date: " + dateParts[1]);
				return;
			}

			Calendar month = Calendar.getInstance();
			month.setTime(date);
			cal.set(Calendar.MONTH, month.get(Calendar.MONTH));
			cal.set(Calendar.YEAR, Integer.parseInt(dateParts[2]));

			updateDateStatement.setDate(1, new Date(cal.getTimeInMillis()));
			updateDateStatement.setString(2, region.substring(0, 2));
			updateDateStatement.setString(3, lineParts[0]);
			updateDateStatement.execute();
			
		}
	}
	
	@Override
	protected void prepareStatements() throws SQLException {
		updateDateStatement = dbcon
				.getConnection()
				.prepareStatement(
						"UPDATE movies SET release_Date = ?, region = ? WHERE title = ?");
	}

	@Override
	protected void closeStatements() throws SQLException {
		updateDateStatement.close();
	}

}
