package de.softwarekollektiv.dbs.queries.simple;

import de.softwarekollektiv.dbs.app.MenuItem;

class QueryA implements MenuItem {

	@Override
	public String getTitle() {
		return "Customer price model rollup";
	}

	@Override
	public String getDescription() {
		return "Beantwortet Aufgabe 5.1.a:\nWie viele Kunden haben sich für welches Preismodell entschieden?";
	}

	@Override
	public boolean run() throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

}
