package de.softwarekollektiv.dbs.parser;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import org.postgresql.util.PSQLException;

import de.softwarekollektiv.dbs.model.Actor;
import de.softwarekollektiv.dbs.model.Movie;
import de.softwarekollektiv.dbs.model.SEX;

public class ActorsParser extends AbstractImdbParser implements ImdbParser {

	Actor currentActor;
	SEX sex;
	private boolean firstLine = true;

	public ActorsParser() {
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
			currentActor = new Actor();

			currentActor.setName(lineParts[0]);

		}
		Movie m = new Movie();


		m.setTitle(getTitleFromImdbString(lineParts[1]));
		m.setReleaseDate(getDateFromImdbString(lineParts[1]));
		currentActor.setMovie(m);

		try {
			st.setString(1, currentActor.getName());
			st.setString(2, currentActor.getMovie().getTitle());
			st.setDate(3, currentActor.getMovie().getReleaseDate());

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

	private String getDateFromImdbString(String text) {
		int first = text.indexOf("(");
		int last = text.indexOf(")");

		String date;
		/*
		 * bsp: Genitori & figli:) - Agitare bene prima dell'uso (2010)
		 */
		try {
			 date = text.substring(first + 1, last);			
		} catch (StringIndexOutOfBoundsException e){
			date = "2000";
		}
		return date.split("/")[0];
	}
}
