package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.model.Movie;
import de.softwarekollektiv.dbs.model.SEX;
import de.softwarekollektiv.dbs.parser.AbstractParser;
import de.softwarekollektiv.dbs.parser.Parser;

public class ActorsParser extends AbstractParser implements Parser {

	String currentActor;
	SEX sex;
	private boolean firstLine = true;
	private PreparedStatement actorsSt;
	private PreparedStatement featuresStatement;
	
	private boolean male;
	
	public ActorsParser(DbConnection dbcon, String file, boolean male) throws SQLException {
		super(dbcon, file);
		this.male = male;
		super.delimiter = "\t+";
		super.firstStop = "----\t\t\t------";
		super.table = "actors";
		super.values = 3;
		
		actorsSt = dbcon.getConnection().prepareStatement(
				"INSERT INTO actors VALUES (DEFAULT, ? , "+male+")");
		
	}

	@Override
	public void newLine(String[] lineParts) {

		
		if (lineParts[0].equals("")) {
			currentActor = null;
			return;
		}

		if (currentActor == null) {
			currentActor = lineParts[0];
		}
	
		String movieTitle = lineParts[0];


		try {
			actorsSt.setString(1, currentActor);
			actorsSt.setString(2, movieTitle);
			actorsSt.setDate(3, movieReleaseDate);

			actorsSt.execute();

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			log.debug("SQLException", e);
		}

	}

	private String getTitleFromImdbString(String text) {
		// TODO Auto-generated method stub

		return text.split(" \\(\\d+.*?\\)")[0];
	}


}
