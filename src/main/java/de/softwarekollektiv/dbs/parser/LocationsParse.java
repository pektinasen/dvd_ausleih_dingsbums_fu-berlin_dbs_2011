package de.softwarekollektiv.dbs.parser;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class LocationsParse extends AbstractImdbParser implements ImdbParser {

	public LocationsParse() {
					// LOCATIONS LIST
		super.firstStop = "==============";
	}
	
	@Override
	public void newLine(String[] lineParts, PreparedStatement st) {
		// TODO Auto-generated method stub

	}

}
