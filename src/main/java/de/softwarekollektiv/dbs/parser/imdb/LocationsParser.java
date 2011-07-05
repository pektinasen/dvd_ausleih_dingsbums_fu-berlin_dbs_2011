package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.PreparedStatement;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.AbstractParser;
import de.softwarekollektiv.dbs.parser.Parser;

public class LocationsParser extends AbstractParser implements Parser {

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
