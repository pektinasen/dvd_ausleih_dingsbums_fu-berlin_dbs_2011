package de.softwarekollektiv.dbs.parser;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.softwarekollektiv.dbs.model.SEX;

public class ActorsParser extends AbstractImdbParser implements ImdbParser {

	private String currentActor;
	private SEX sex;

	public ActorsParser() {
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
