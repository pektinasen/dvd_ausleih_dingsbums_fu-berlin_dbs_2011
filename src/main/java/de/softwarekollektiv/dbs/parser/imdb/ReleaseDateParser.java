package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.Parser;


public class ReleaseDateParser extends AbstractImdbParser implements Parser {

	private final DbConnection dbcon;

	private PreparedStatement updateDateStatement;

	/*
	 * group(1) = day
	 * group(2) = month year
	 * group(3) = month
	 * group(4) = year
	 */
	private static final Pattern datePattern = Pattern
			.compile("(\\d{1,2})? ?((\\w+)? ?(\\d{4}))");

	public ReleaseDateParser(DbConnection dbcon, String file) {
		super(dbcon, file);
		super.skipLines = 14;

		this.dbcon = dbcon;
	}

	@Override
	protected void newLine(String[] lineParts) throws SQLException {

		String movieTitle = lineParts[0];
		String[] dateParts;
		if (lineParts.length >= 2) {
			dateParts = lineParts[1].split(":");
		} else {
			dateParts = new String[] { "2010" };
		}
		String dateString;
		String dateRegion;
		if (dateParts.length >= 2) {
			dateRegion = dateParts[0];
			dateString = dateParts[1];
		}
		// dateParts.length == 1
		else {
			dateRegion = "unknown";
			dateString = dateParts[0];
		}

		Matcher m = datePattern.matcher(dateString);
		m.find();
		String day = m.group(1);
		String month = m.group(3);
		String year = m.group(4);

		Date date = toDate(day, month, year);
		Date minDate = toDate("1", "Jan", "2010");

		if (date.compareTo(minDate) >= 0) {

			updateDateStatement.setDate(1, date);
			updateDateStatement.setString(2, dateRegion);
			updateDateStatement.setString(3, movieTitle);

			updateDateStatement.addBatch();
		}

	}

	private Date toDate(String day, String month, String year) {
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.DAY_OF_MONTH,
				(day == null) ? 1 : Integer.parseInt(day));

		java.util.Date monthDate = null;
		try {
			monthDate = new SimpleDateFormat("MMM", Locale.ENGLISH)
					.parse(month == null ? "JAN" : month.substring(0, 3));

		} catch (ParseException e) {
			log.error("this should not happen: " + month, e);
		}

		Calendar calMonth = Calendar.getInstance();
		calMonth.setTime(monthDate);
		cal.set(Calendar.MONTH, calMonth.get(Calendar.MONTH));
		cal.set(Calendar.YEAR, Integer.parseInt(year));

		return new Date(cal.getTimeInMillis());
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

	@Override
	protected void executeBatchStatements() throws SQLException {
		updateDateStatement.executeBatch();
	}

}
