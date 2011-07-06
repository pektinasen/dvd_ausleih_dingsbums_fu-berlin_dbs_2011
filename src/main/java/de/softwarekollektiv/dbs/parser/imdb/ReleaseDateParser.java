package de.softwarekollektiv.dbs.parser.imdb;

import java.sql.SQLException;

import de.softwarekollektiv.dbs.dbcon.DbConnection;

public class ReleaseDateParser extends AbstractImdbParser {

	public ReleaseDateParser(DbConnection dbcon, String file) {
		super(dbcon, file);
	}

	@Override
	protected void newLine(String[] lineParts) throws SQLException {
		// TODO Auto-generated method stub
		
	}
}
