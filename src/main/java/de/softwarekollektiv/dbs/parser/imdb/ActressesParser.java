package de.softwarekollektiv.dbs.parser.imdb;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.Parser;

public class ActressesParser extends ActorsParser implements Parser {

	public ActressesParser(DbConnection dbcon, String file) {
		super(dbcon, file);
		super.skipLines = 241;
		super.stopAfter = 6572668;
		super.male = false;
	}

}
