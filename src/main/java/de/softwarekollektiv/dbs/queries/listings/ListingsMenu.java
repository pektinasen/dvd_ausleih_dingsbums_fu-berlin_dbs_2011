package de.softwarekollektiv.dbs.queries.listings;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import de.softwarekollektiv.dbs.app.AbstractSelectionMenu;
import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.app.QuitItem;
import de.softwarekollektiv.dbs.dbcon.DbConnection;

public class ListingsMenu extends AbstractSelectionMenu implements MenuItem {

	private final List<MenuItem> items;
	
	public ListingsMenu(PrintStream out, InputStream in, DbConnection dbcon) {
		super(out, in);
		
		items = new LinkedList<MenuItem>();
		items.add(new CustomerListing(out, dbcon));
		items.add(new MovieListing(out, dbcon));
		items.add(new QuitItem("Return", "Return to main menu."));
	}

	@Override
	public String getTitle() {
		return "Listings";
	}

	@Override
	public String getDescription() {
		return "Print some listings, such as all movie titles, all customers or all actors.";
	}

	@Override
	protected List<MenuItem> getItems() {
		return items;
	}

	@Override
	protected String getGreeter() {
		return "Listings";
	}

}
