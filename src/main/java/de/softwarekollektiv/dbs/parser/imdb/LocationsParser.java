package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.Parser;

public class LocationsParser extends AbstractImdbParser implements Parser {
	private final DbConnection dbcon;
	private final Map<String, Integer> movIdCache;
	private final Map<String, Integer> locIdCache;
	
	private PreparedStatement locInsStmt;
	private PreparedStatement shotInStmt;
	private int nextId = 1;

	public LocationsParser(DbConnection dbcon, String file, Map<String, Integer> movIdCache) {
		super(dbcon, file);
		super.skipLines = 264;
		super.stopAfter = 532964;
		this.dbcon = dbcon;
		this.movIdCache = movIdCache;
		this.locIdCache = new HashMap<String, Integer>();
	}

	@Override
	protected void newLine(String[] lineParts) throws SQLException {
		
		// TODO HACK
		if(lineParts.length < 2){
			log.debug("too short: "+ Arrays.toString(lineParts));
			return;
		}
		
		String movieTitle = lineParts[0];
		String location = lineParts[1];
		String[] locationParts = location.split(",");
		String country = locationParts[locationParts.length - 1].trim();
		
		Integer movId = movIdCache.get(movieTitle);
		
		// only when the movie exists
		if(movId != null) {
			
			Integer locId = locIdCache.get(location);
			
			// If location has not been inserted, insert it now
			if(locId == null) {
				locId = nextId++;
				
				locInsStmt.setInt(1, locId);
				locInsStmt.setString(2, location);
				locInsStmt.setString(3, country);
				locInsStmt.addBatch();
				
				locIdCache.put(location, locId);
			}
			
			shotInStmt.setInt(1, movId);
			shotInStmt.setInt(2, locId);
			shotInStmt.addBatch();
		}
	}

	@Override
	protected void prepareStatements() throws SQLException {
		locInsStmt = dbcon.getConnection().prepareStatement(
				"INSERT INTO locations VALUES (?, ?, ?)");
		shotInStmt = dbcon.getConnection().prepareStatement(
				"INSERT INTO shotIn VALUES (?, ?);");
	}

	@Override
	protected void closeStatements() throws SQLException {
		locInsStmt.close();
		shotInStmt.close();
	}

	@Override
	protected void executeBatchStatements() throws SQLException {
		locInsStmt.executeBatch();
		shotInStmt.executeBatch();
	}

}
