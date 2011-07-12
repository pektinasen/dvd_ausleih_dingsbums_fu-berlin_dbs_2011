package de.softwarekollektiv.dbs.queries.complex;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import de.softwarekollektiv.dbs.app.AbstractSelectionMenu;
import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.app.QuitItem;
import de.softwarekollektiv.dbs.dbcon.DbConnection;

public class ComplexQueryMenu extends AbstractSelectionMenu implements MenuItem {

	private final List<MenuItem> items;
	
	public ComplexQueryMenu(PrintStream out, InputStream in, DbConnection dbcon) {
		super(out, in);
		
		items = new LinkedList<MenuItem>();
		items.add(new QueryE(out, dbcon));
		items.add(new QueryF(out, dbcon));
		items.add(new QuitItem("Return", "Return to main menu."));
	}
	
	@Override
	protected List<MenuItem> getItems() {
		return items;
	}

	@Override
	protected String getGreeter() {
		return "Complex queries (as required by assignment 5.2)";
	}

	@Override
	public String getTitle() {
		return "Complex queries";
	}

	@Override
	public String getDescription() {
		return "Run the complex Java+SQL queries as required in the project description.";
	}


}
