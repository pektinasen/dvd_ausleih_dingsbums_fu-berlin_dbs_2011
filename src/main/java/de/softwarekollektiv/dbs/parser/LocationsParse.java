package de.softwarekollektiv.dbs.parser;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LocationsParse extends AbstractImdbParser implements ImdbParser {

	private boolean firstLine = true;

	public LocationsParse() {
						// LOCATIONS LIST
		super.firstStop = "==============";
		super.delimiter = "\t+";
		super.table = "locations";
		super.values = 3;
	}

	@Override
	public void newLine(String[] lineParts, PreparedStatement st) {
		if (firstLine) {
			firstLine = false;
			return;
		}

		String location = lineParts[1].split("|")[0];
		String movieTitle = getTitleFromImdbString(lineParts[0]);
		Date movieReleaseDate = getDateFromImdbString(lineParts[0]);

		try {
			st.setString(1, location);
			st.setString(2, movieTitle);
			st.setDate(3, movieReleaseDate);

			st.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
