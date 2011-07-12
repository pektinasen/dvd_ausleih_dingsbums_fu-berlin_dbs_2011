package de.softwarekollektiv.dbs.queries.complex;

import java.io.PrintStream;

import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.dbcon.DbConnection;

class QueryF implements MenuItem {

	private final PrintStream out;
	private final DbConnection dbcon;
	
	QueryF(PrintStream out, DbConnection dbcon) {
		this.out = out;
		this.dbcon = dbcon;
	}

	@Override
	public String getTitle() {
		return "Shortest path";
	}

	@Override
	public String getDescription() {
		return "Beantwortet Aufgabe 5.2.b:\n[...]\nErmitteln Sie nun die kürzeste Verbindung zwischen folgenden Paaren:\ni. Johnny Depp und Timothy Dalton\nii. Johnny Depp und August Diehl\niii. Bill Murray und Sylvester Stallone\niv. Edward Norton und Don Cheadle";
	}

	@Override
	public boolean run() throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

}
