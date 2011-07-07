package de.softwarekollektiv.dbs.queries.simple;

import java.io.IOException;
import java.io.PrintStream;

import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.app.Utils;
import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.queries.AbstractSQLQuery;

class QueryB extends AbstractSQLQuery implements MenuItem {

	QueryB(PrintStream out, DbConnection dbcon) {
		super(out, dbcon);
	}

	@Override
	public String getTitle() {
		return "Invoices (January)";
	}

	@Override
	public String getDescription() {
		return "Beantwortet Aufgabe 5.1.b:\nDie Rechnungssummen für alle Kunden für den Monat Januar.";
	}

	@Override
	protected String getQuery() throws IOException {
		return Utils.fileToString("src/main/resources/sql/queryb.sql");
	}

//	@Override
	protected String[] getResultFields() {
		// TODO Auto-generated method stub
		return null;
	}

}
