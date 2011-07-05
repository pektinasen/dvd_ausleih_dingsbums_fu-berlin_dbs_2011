package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
<<<<<<< HEAD

=======
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.model.Movie;
>>>>>>> 02a008d60f5c890114d97517a77ec96b835249ba
import de.softwarekollektiv.dbs.model.SEX;
import de.softwarekollektiv.dbs.parser.AbstractParser;
import de.softwarekollektiv.dbs.parser.Parser;

public class ActorsParser extends AbstractParser implements Parser {

	private String currentActor;
	private SEX sex;

	public ActorsParser(DbConnection dbcon, String file) {
		super(dbcon, file);
		super.delimiter = "\t+";
		super.firstStop = "----\t\t\t------";
		super.table = "actors";
		super.values = 3;
		super.ref_id = "(Selec)";
	}

	@Override
	public void newLine(String[] lineParts, PreparedStatement st) {

		if (lineParts[0].equals("")) {
			currentActor = null;
			return;
		}

		if (currentActor == null) {

			currentActor = lineParts[0];

		}

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


}
