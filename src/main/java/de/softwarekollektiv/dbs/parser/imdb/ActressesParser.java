package de.softwarekollektiv.dbs.parser.imdb;

import java.util.Map;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.parser.Parser;

public class ActressesParser extends ActorsParser implements Parser {

	public ActressesParser(DbConnection dbcon, String file, Map<String, Integer> movIdCache) {
		super(dbcon, file, movIdCache);
		super.skipLines = 241;
		super.stopAfter = 6572668;
		super.male = false;
	}

}
