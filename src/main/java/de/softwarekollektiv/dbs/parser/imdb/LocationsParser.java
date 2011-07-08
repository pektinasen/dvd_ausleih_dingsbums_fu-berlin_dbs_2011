package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.Parser;

public class LocationsParser extends AbstractImdbParser implements Parser {
	private final DbConnection dbcon;
	
	private PreparedStatement movIdStmt;
	private PreparedStatement locIdStmt;
	private PreparedStatement locInsStmt;
	private PreparedStatement shotInStmt;

	public LocationsParser(DbConnection dbcon, String file) {
		super(dbcon, file);
		super.skipLines = 264;
		this.dbcon = dbcon;
	}

	@Override
	protected void newLine(String[] lineParts) throws SQLException {
		
		// TODO HACK
		if(lineParts.length < 2){
			log.debug("too short: "+ Arrays.toString(lineParts));
			return;
		}
		
		String movieTitle = lineParts[0];
		String[] locationParts = lineParts[1].split(",");
		String country = locationParts[locationParts.length - 1];
		
		int movId;
		int locId;
		
		movIdStmt.setString(1, movieTitle);
		ResultSet movIdRslt = movIdStmt.executeQuery();
		
		// Is movie in database?
		if(movIdRslt.next()) {
			movId = movIdRslt.getInt(1);
			
			locIdStmt.setString(1, lineParts[1]);
			ResultSet locIdRslt = locIdStmt.executeQuery();
			
			// Is location already in database?
			if(locIdRslt.next()) {
				locId = locIdRslt.getInt(1);
			} else {
				locInsStmt.setString(1, lineParts[1]);
				locInsStmt.setString(2, country);
				locInsStmt.execute();

				ResultSet locInsRslt = locInsStmt.getGeneratedKeys();
				locInsRslt.next();
				locId = locInsRslt.getInt(1);
				locInsRslt.close();
			}
			locIdRslt.close();
			
			shotInStmt.setInt(1, movId);
			shotInStmt.setInt(2, locId);
			shotInStmt.execute();
		}
		movIdRslt.close();
	}

	@Override
	protected void prepareStatements() throws SQLException {
		movIdStmt = dbcon.getConnection().prepareStatement(
				"SELECT mov_id FROM movies WHERE title = ?;");
		locIdStmt = dbcon.getConnection().prepareStatement(
				"SELECT loc_id FROM locations WHERE name = ?;");
		locInsStmt = dbcon.getConnection().prepareStatement(
				"INSERT INTO locations VALUES (DEFAULT, ?, ?)",
				Statement.RETURN_GENERATED_KEYS);
		shotInStmt = dbcon.getConnection().prepareStatement(
				"INSERT INTO shotIn VALUES (?, ?);");
	}

	@Override
	protected void closeStatements() throws SQLException {
		movIdStmt.close();
		locIdStmt.close();
		locInsStmt.close();
		shotInStmt.close();
	}

}
