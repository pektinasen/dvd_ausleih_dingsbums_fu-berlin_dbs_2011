package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.AbstractParser;
import de.softwarekollektiv.dbs.parser.Parser;

public class LocationsParser extends AbstractParser implements Parser {

	private PreparedStatement locationsStatement;
	private PreparedStatement lastLocationsSerial;
	private PreparedStatement shotInStatement;

	public LocationsParser(DbConnection dbcon, String file) throws SQLException {
		super(dbcon, file);
		// LOCATIONS LIST
		super.firstStop = "==============";

		locationsStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO locations VALUES (DEFAULT, ?)");

		shotInStatement = dbcon.getConnection().prepareStatement(
				"INSERT INTO shotIn VALUES (?, ("
						+ "		SELECT mov_id FROM movies WHERE title = ?)" + ")");

		lastLocationsSerial = dbcon.getConnection().prepareStatement(
				"SELECT is_called, last_Value FROM locations_act_id_seq;");
	}

	@Override
	public void newLine(String[] lineParts) {

		String location = lineParts[1].split("|")[0];
		String movieTitle = lineParts[0];

		try {
			locationsStatement.setString(1, location);

			locationsStatement.execute();

			ResultSet lastValue = lastLocationsSerial.executeQuery();
			lastValue.next();
			int nextLocId = 1;
			if (lastValue.getBoolean(1)) {
				nextLocId = lastValue.getInt(2) + 1;
			}

			shotInStatement.setInt(1, nextLocId);
			shotInStatement.setString(2, movieTitle);
			shotInStatement.execute();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
