package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.Date;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.AbstractParser;

abstract class AbstractImdbParser extends AbstractParser {

	private static final Pattern yearPtrn = Pattern
			.compile("\\(\\d{4}-?(\\d{4}|\\?{4})?\\)");

	protected AbstractImdbParser(DbConnection dbcon, String file) {
		super(dbcon, file);
		super.delimiter = "\t+";
	}

	/**
	 * returns the date of the first day of the year from the given String.
	 * example: (2001), (2001-2002), (2002-????)
	 */
	protected static Date getDateFromImdbString(String text) {
		Matcher matcher = yearPtrn.matcher(text);
		String year;
		if (matcher.find()) {
			year = matcher.group().substring(1, 5);
		} else {
			// entries without a year are discarded since we just insert
			// movies from 2010 and 2011
			year = "2000";
		}

		return getFirstDayOfYear(year);
	}

	protected static Date getFirstDayOfYear(String year) {

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.YEAR, Integer.parseInt(year));
		
		cal.set(Calendar.MONTH, 0);
		cal.set(Calendar.DAY_OF_MONTH, 1);
		return new Date(cal.getTimeInMillis());
	}

}
