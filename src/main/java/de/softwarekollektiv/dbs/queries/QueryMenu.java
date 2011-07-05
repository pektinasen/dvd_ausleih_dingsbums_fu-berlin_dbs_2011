package de.softwarekollektiv.dbs.queries;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.app.SelectionMenu;
import de.softwarekollektiv.dbs.dbcon.DbConnection;

public class QueryMenu extends SelectionMenu implements MenuItem {

	private List<MenuItem> items;
	
	public QueryMenu(PrintStream out, InputStream in, DbConnection dbcon) {
		super(out, in);
		
		items = new LinkedList<MenuItem>();
		items.add(new QueryA());
//		items.add(new QueryB());
//		items.add(new QueryC());
//		items.add(new QueryD());
//		items.add(new QueryE());
//		items.add(new QueryF());
	}
	
	@Override
	protected List<MenuItem> getItems() {
		return items;
	}

	@Override
	protected String getGreeter() {
		return "Queries (as required)";
	}

	@Override
	public String getTitle() {
		return "Queries";
	}

	@Override
	public String getDescription() {
		return "Run the queries as required in the project description.";
	}

}
