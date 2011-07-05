package de.softwarekollektiv.dbs.parser;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.softwarekollektiv.dbs.DbConnection;
import de.softwarekollektiv.dbs.model.Movie;
import de.softwarekollektiv.dbs.model.SEX;

public class ActorsParser extends AbstractImdbParser implements ImdbParser {

	String currentActor;
	SEX sex;
	private boolean firstLine = true;

	public ActorsParser(DbConnection dbcon, String file) {
		super(dbcon, file);
		super.delimiter = "\t+";
		super.firstStop = "----\t\t\t------";
		super.table = "actors";
		super.values = 3;
	}

	@Override
	public void newLine(String[] lineParts, PreparedStatement st) {

		
		if (lineParts[0].equals("")) {
			/*
			 * workaround for for first empty line in actresses.list
			 */
			currentActor = null;
			return;
		}

		if (currentActor == null) {

			currentActor = lineParts[0];

		}
		Movie m = new Movie();

		
		String movieTitle;
		Date movieReleaseDate;
		movieTitle = getTitleFromImdbString(lineParts[1]);
		movieReleaseDate = getDateFromImdbString(lineParts[1]);


		try {
			st.setString(1, currentActor);
			st.setString(2, movieTitle);
			st.setDate(3, movieReleaseDate);

			st.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.debug("SQLException", e);
		}

	}

	private String getTitleFromImdbString(String text) {
		// TODO Auto-generated method stub

		return text.split(" \\(\\d+.*?\\)")[0];
	}

	private Date getDateFromImdbString(String text) {
		Pattern pattern = Pattern.compile("\\(\\d{4}-?(\\d{4}|\\?{4})?\\)");
		
		Matcher matcher = pattern.matcher(text);
		String year;
		if (matcher.find()){
			year = matcher.group().substring(1,5);
		}
		else {
			//entries without a year are discarded since we just insert
			//movies from 2010 and 2011
			year = "2000";
		}
		
		Calendar cal = Calendar.getInstance();
	
			cal.set(Calendar.YEAR, Integer.parseInt(year));
			cal.set(Calendar.MONTH, 1);
			cal.set(Calendar.DAY_OF_MONTH, 1);

 		
		
		return new Date(cal.getTimeInMillis());
	}
}
