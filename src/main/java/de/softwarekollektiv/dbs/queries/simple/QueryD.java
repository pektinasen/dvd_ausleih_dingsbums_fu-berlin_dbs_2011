package de.softwarekollektiv.dbs.queries.simple;

import java.io.PrintStream;

import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.queries.AbstractSQLQuery;

class QueryD extends AbstractSQLQuery implements MenuItem {

	QueryD(PrintStream out, DbConnection dbcon) {
		super(out, dbcon);
	}

	@Override
	public String getTitle() {
		return "Customers with high foreign movie / US-movie ratio";
	}

	@Override
	public String getDescription() {
		return "Beantwortet Aufgabe 5.1.d:\nErmitteln sie eine Rangliste alle Kunden, sortiert nach dem\nAnteil der geliehenen Filme, die (auch) außerhalb der USA\ngedreht wurden.";
	}

	@Override
	protected String getQuery() {
		return "SELECT cus_id, COUNT(*) as num FROM rentals r WHERE r.mov_id IN (SELECT DISTINCT mov_id FROM shotIn s JOIN locations l ON s.loc_id = l.loc_id WHERE l.country <> 'USA') GROUP BY r.cus_id;";
	}

	@Override
	protected String[] getResultFields() {
		return new String[] { "cus_id", "num" };
	}

}
