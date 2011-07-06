package de.softwarekollektiv.dbs.parser.misc;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.AbstractParser;

public class RentalsParser extends AbstractParser {

	private final PreparedStatement rentalsStatement;

	public RentalsParser(DbConnection dbcon, String file) throws SQLException {
		super(dbcon, file);
		super.delimiter = "\t";

		rentalsStatement = dbcon.getConnection().prepareStatement(
						"INSERT INTO rentals VALUES (?, ?, (SELECT mov_id FROM MOVIES WHERE title = ?), ?, ?);");
	}

	@Override
	protected void newLine(String[] lineParts) throws SQLException {
		int id = Integer.parseInt(lineParts[0]);
		String type = lineParts[1];
		String movtitle = lineParts[2];
		Date date = Date.valueOf(lineParts[3]);
		int duration = Integer.parseInt(lineParts[4]);
		
		rentalsStatement.setInt(1, id);
		rentalsStatement.setString(2, type);
		rentalsStatement.setString(3, movtitle);
		rentalsStatement.setDate(4, date);
		rentalsStatement.setInt(5, duration);
		
		rentalsStatement.execute();
	}

	@Override
	protected void skipHeader(BufferedReader in) throws IOException {
		in.readLine();
	}

}
