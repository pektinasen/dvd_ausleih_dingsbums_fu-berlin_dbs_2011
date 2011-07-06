package de.softwarekollektiv.dbs.parser.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.AbstractParser;

public class RentalsParser extends AbstractParser {
	private static final Logger log = Logger.getLogger(RentalsParser.class);
	
	
	private final PreparedStatement rentalsStatement;
	private final SimpleDateFormat dateFormat;

	public RentalsParser(DbConnection dbcon, String file) throws SQLException {
		super(dbcon, file);
		super.delimiter = "\t";

		rentalsStatement = dbcon.getConnection().prepareStatement(
						"INSERT INTO rentals VALUES (?, ?, (SELECT mov_id FROM MOVIES WHERE title = ?), ?, ?);");
		
		dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
	}

	@Override
	protected void newLine(String[] lineParts) throws SQLException {
		int id = Integer.parseInt(lineParts[0]);
		String type = lineParts[1];
		String movtitle = lineParts[2];
		int duration = Integer.parseInt(lineParts[4]);
		
		Timestamp ts = null;
		try {
			Date dt = dateFormat.parse(lineParts[3]);
			ts = new Timestamp(dt.getTime());
		} catch (ParseException e) {
			log.warn("Could not parse date: " + lineParts[3]);
			return;
		}
		
		rentalsStatement.setInt(1, id);
		rentalsStatement.setString(2, type);
		rentalsStatement.setString(3, movtitle);
		rentalsStatement.setTimestamp(4, ts);
		rentalsStatement.setInt(5, duration);

		rentalsStatement.execute();
	}

	@Override
	protected void skipHeader(BufferedReader in) throws IOException {
		in.readLine();
	}
}
