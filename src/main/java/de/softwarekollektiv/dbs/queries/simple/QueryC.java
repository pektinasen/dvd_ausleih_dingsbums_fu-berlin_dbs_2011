package de.softwarekollektiv.dbs.queries.simple;

import java.io.PrintStream;

import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.queries.AbstractSQLQuery;

class QueryC extends AbstractSQLQuery implements MenuItem {

	QueryC(PrintStream out, DbConnection dbcon) {
		super(out, dbcon);
	}

	@Override
	public String getTitle() {
		return "Best release month for Oscar nomination";
	}

	@Override
	public String getDescription() {
		return "Beantwortet Aufgabe 5.1.c:\nSollte man bestimmte Monate für eine Premiere wählen, um für\neinen Oscar in den Kategorien Bester Film/Regisseur/\nHauptdarsteller oder Hauptdarstellerin nominiert zu werden?";
	}

	@Override
	protected String getQuery() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String[] getResultFields() {
		// TODO Auto-generated method stub
		return null;
	}

}
