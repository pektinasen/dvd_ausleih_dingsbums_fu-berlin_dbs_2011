package de.softwarekollektiv.dbs.parser.misc;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.AbstractParser;

public class RentalsParser extends AbstractParser {
	private static final Logger log = Logger.getLogger(RentalsParser.class);
	private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy hh:mm:ss");
	private final DbConnection dbcon;
	private final Map<String, Integer> movIdCache;
	
	private PreparedStatement rentalsStmt;

	public RentalsParser(DbConnection dbcon, String file, Map<String, Integer> movIdCache) throws SQLException {
		super(dbcon, file);
		super.delimiter = "\t";
		super.skipLines = 1;

		this.dbcon = dbcon;
		this.movIdCache = movIdCache;
	}

	@Override
	protected void newLine(String[] lineParts) throws SQLException {
		int id = Integer.parseInt(lineParts[0]);
		String type = lineParts[1];
		String movtitle = lineParts[2];
		int duration = Integer.parseInt(lineParts[4]);
		
		Integer movId = movIdCache.get(movtitle);
		if(movId != null) {
		
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
	
			rentalsStmt.addBatch();
			
		}
	}

	@Override
	protected void prepareStatements() throws SQLException {
		rentalsStmt = dbcon.getConnection().prepareStatement(
						"INSERT INTO rentals VALUES (?, ?, ?, ?, ?);");
	}

	@Override
	protected void closeStatements() throws SQLException {
		rentalsStmt.close();
	}

	@Override
	protected void executeBatchStatements() throws SQLException {
		rentalsStmt.executeBatch();
	}
}
