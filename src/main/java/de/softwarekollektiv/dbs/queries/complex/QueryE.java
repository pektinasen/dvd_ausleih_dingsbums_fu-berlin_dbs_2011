package de.softwarekollektiv.dbs.queries.complex;

import java.io.PrintStream;

import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.dbcon.DbConnection;

class QueryE implements MenuItem {

	private final PrintStream out;
	private final DbConnection dbcon;
	
	QueryE(PrintStream out, DbConnection dbcon) {
		this.out = out;
		this.dbcon = dbcon;
	}

	@Override
	public String getTitle() {
		return "Flat customers who paid more than they would have with starter.";
	}

	@Override
	public String getDescription() {
		return "Beantwortet Aufgabe 5.2.a:\nBestimmen Sie alle Kunden mit Preismodell Flat, welche\nbasierend auf ihren bisherigen Ausleihvorgängen im Modell\nStarter billiger weggekommen wären.";
	}

	@Override
	public boolean run() throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

}
