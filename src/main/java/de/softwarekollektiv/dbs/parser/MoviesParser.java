package de.softwarekollektiv.dbs.parser;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.model.Movie;

/*
 * FIXME Date ist nicht richtig implementiert
 */
public class MoviesParser extends AbstractImdbParser implements ImdbParser {

	
	/*
	 * delimiter for coloumns in imdb file as RegEx
	 */
	

	public MoviesParser(DbConnection dbcon, String file) {
		super(dbcon, file);
		super.delimiter = "\t+";
		super.firstStop = "title\tyear\tcategory";
		super.table = "movies";
		super.values = 2;
	}

	@Override
	public void newLine(String[] lineParts, PreparedStatement st) {
		/*
		 * now process data
		 */
		
		/*
		 * "<title>" -> tv series
		 * title can be any character
		 */
		if (lineParts[0].matches("\".+?\".*")){
//			log.debug(lineParts[0]);
			return; 
		}
		/*
		 * Movie Object parse and encode the String
		 */
		try {
			Movie m = new Movie();
			/*
			 * TODO soll die Jahreszahl zum Titel gehören
			 */
			m.setTitle(lineParts[0].split(" \\(\\d+.*?\\)")[0]);
			m.setReleaseDate(lineParts[1]);

			/*
			 * TODO make it more orm-like
			 * m.save();
			 * maybe m.save(dbConnection);
			 */
			st.setString(1, m.getTitle());
			st.setDate(2, m.getReleaseDate());
			st.execute();
		} catch (SQLException e) {
			
			log.error(Arrays.toString(lineParts), e);

		} catch (Exception ee) {
			log.error(Arrays.toString(lineParts));
		}
		

	}

}
