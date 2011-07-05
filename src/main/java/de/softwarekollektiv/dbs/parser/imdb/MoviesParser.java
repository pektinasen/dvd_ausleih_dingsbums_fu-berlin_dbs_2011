package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

<<<<<<< HEAD
public class MoviesParser extends AbstractImdbParser implements ImdbParser {
=======
import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.model.Movie;
import de.softwarekollektiv.dbs.parser.AbstractParser;
import de.softwarekollektiv.dbs.parser.Parser;

/*
 * FIXME Date ist nicht richtig implementiert
 */
public class MoviesParser extends AbstractParser implements Parser {
>>>>>>> 02a008d60f5c890114d97517a77ec96b835249ba

	/*
	 * delimiter for coloumns in imdb file as RegEx
	 */

	public MoviesParser(DbConnection dbcon, String file) {
		super(dbcon, file);
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
