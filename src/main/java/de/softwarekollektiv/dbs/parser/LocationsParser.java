package de.softwarekollektiv.dbs.parser;

import java.sql.PreparedStatement;

import de.softwarekollektiv.dbs.DbConnection;

public class LocationsParser extends AbstractImdbParser implements ImdbParser {

	public LocationsParser(DbConnection dbcon, String file) {
		super(dbcon, file);
					// LOCATIONS LIST
		super.firstStop = "==============";
	}
	
	@Override
	public void newLine(String[] lineParts, PreparedStatement st) {
		// TODO Auto-generated method stub

	}

}
