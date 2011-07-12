package de.softwarekollektiv.dbs.parser.imdb;

import java.util.Map;

import de.softwarekollektiv.dbs.dbcon.DbConnection;

public class AdditionalMoviesParser extends MoviesParser {

	public AdditionalMoviesParser(DbConnection dbcon, String file,
			Map<String, Integer> movIdCache) {
		super(dbcon, file, movIdCache);
		super.skipLines = 1;
	}

}
