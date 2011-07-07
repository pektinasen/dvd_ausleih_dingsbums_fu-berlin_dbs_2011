package de.softwarekollektiv.dbs.queries.simple;

import java.io.IOException;
import java.io.PrintStream;

import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.app.Utils;
import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.queries.AbstractSQLQuery;

class QueryA extends AbstractSQLQuery implements MenuItem {

	QueryA(PrintStream out, DbConnection dbcon) {
		super(out, dbcon);
	}

	@Override
	public String getTitle() {
		return "Customer price model rollup";
	}

	@Override
	public String getDescription() {
		return "Beantwortet Aufgabe 5.1.a:\nWie viele Kunden haben sich für welches Preismodell entschieden?";
	}

	@Override
	protected String getQuery() throws IOException {
		return Utils.fileToString("src/main/resources/sql/querya.sql");
	}
}
