package de.softwarekollektiv.dbs.parser.misc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.AbstractParser;

public class RentalsParser extends AbstractParser {
	private static final Logger log = Logger.getLogger(RentalsParser.class);
	private final DbConnection dbcon;
	private final SimpleDateFormat dateFormat;
	
	private PreparedStatement rentalsStmt;
	private PreparedStatement movIdStmt;

	public RentalsParser(DbConnection dbcon, String file) throws SQLException {
		super(dbcon, file);
		super.delimiter = "\t";
		super.skipLines = 1;

		this.dbcon = dbcon;
		this.dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
	}

	@Override
	protected void newLine(String[] lineParts) throws SQLException {
		int id = Integer.parseInt(lineParts[0]);
		String type = lineParts[1];
		String movtitle = lineParts[2];
		int duration = Integer.parseInt(lineParts[4]);
		
		movIdStmt.setString(1, movtitle);
		ResultSet movIdRslt = movIdStmt.executeQuery();
		if(movIdRslt.next()) {
			int movId = movIdRslt.getInt(1);
		
			Timestamp ts = null;
			try {
				Date dt = dateFormat.parse(lineParts[3]);
				ts = new Timestamp(dt.getTime());
			} catch (ParseException e) {
				log.warn("Could not parse date: " + Arrays.toString(lineParts));
				return;
			}
			
			rentalsStmt.setInt(1, id);
			rentalsStmt.setString(2, type);
			rentalsStmt.setInt(3, movId);
			rentalsStmt.setTimestamp(4, ts);
			rentalsStmt.setInt(5, duration);
	
			rentalsStmt.execute();
			
		}
	}

	@Override
	protected void prepareStatements() throws SQLException {
		movIdStmt = dbcon.getConnection().prepareStatement(
						"SELECT mov_id FROM movies WHERE title = ?;");
		rentalsStmt = dbcon.getConnection().prepareStatement(
						"INSERT INTO rentals VALUES (?, ?, ?, ?, ?);");
	}

	@Override
	protected void closeStatements() throws SQLException {
		movIdStmt.close();
		rentalsStmt.close();
	}
}
