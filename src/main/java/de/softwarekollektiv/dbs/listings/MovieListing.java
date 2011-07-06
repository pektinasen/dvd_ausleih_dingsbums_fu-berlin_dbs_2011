package de.softwarekollektiv.dbs.listings;

import java.io.PrintStream;

import de.softwarekollektiv.dbs.dbcon.DbConnection;

class MovieListing extends AbstractListing {

	MovieListing(PrintStream out, DbConnection dbcon) {
		super(out, dbcon);
	}

	@Override
	public String getTitle() {
		return "Movies";
	}

	@Override
	public String getDescription() {
		return "Prints a list of all available movies.";
	}

	@Override
	protected String getTable() {
		return "movies";
	}

	@Override
	protected String[] getFields() {
		return new String[] { "title" };
	}

}
