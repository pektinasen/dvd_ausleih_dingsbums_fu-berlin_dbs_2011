package de.softwarekollektiv.dbs.parser;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

public class MoviesParser extends AbstractImdbParser implements ImdbParser {

	/*
	 * delimiter for coloumns in imdb file as RegEx
	 */

	public MoviesParser() {
		super.delimiter = "\t+";
		super.firstStop = "title\tyear\tcategory";
		super.table = "movies";
		super.valuesSize = 3;
		super.values = "(title, release_date)";
	}

	@Override
	public void newLine(String[] lineParts, PreparedStatement st) {
		/*
		 * "<title>" -> tv series title can be any character
		 */
		if (lineParts[0].matches("\".+?\".*")) {
			// log.debug(lineParts[0]);
			return;
		}

		try {
			String title = lineParts[0].split(" \\(\\d+.*?\\)")[0];
			String releaseDate = lineParts[1];

			st.
			st.setString(2, title);
			st.setDate(3, yearToDate(releaseDate));
			st.execute();
		} catch (SQLException e) {

			log.error(Arrays.toString(lineParts), e);

		} catch (Exception ee) {
			log.error(Arrays.toString(lineParts));
		}
	}
}
