package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.Parser;

public class LocationsParser extends AbstractImdbParser implements Parser {
	private static final Logger log = Logger.getLogger(LocationsParser.class);
	private final DbConnection dbcon;
	
	private PreparedStatement movIdStmt;
	private PreparedStatement locIdStmt;
	private PreparedStatement locInsStmt;
	private PreparedStatement shotInStmt;

	public LocationsParser(DbConnection dbcon, String file) throws SQLException {
		super(dbcon, file);
		super.delimiter = "\t+";
		super.firstStop = "==============";

		this.dbcon = dbcon;
	}

	@Override
	protected void newLine(String[] lineParts) {

		// TODO HACK
		if(lineParts.length < 2)
			return;
		
		String movieTitle = lineParts[0];
		String location = lineParts[1].split("|")[0];
		String[] locationParts = location.split(",");
		String country = locationParts[locationParts.length - 1];
		
		int movId;
		int locId;
		
		try {
			
			movIdStmt.setString(1, movieTitle);
			ResultSet movIdRslt = movIdStmt.executeQuery();
			
			// Is movie in database?
			if(movIdRslt.next()) {
				movId = movIdRslt.getInt(1);
				
				locIdStmt.setString(1, location);
				ResultSet locIdRslt = locIdStmt.executeQuery();
				
				// Is location already in database?
				if(locIdRslt.next()) {
					locId = locIdRslt.getInt(1);
				} else {
					locInsStmt.setString(1, location);
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
			
		} catch (SQLException e) {
			// TODO eliminate this
			log.debug("SQLException", e);
		}
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
