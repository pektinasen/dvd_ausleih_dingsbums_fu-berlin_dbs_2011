package de.softwarekollektiv.dbs.dbcon;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.app.QuitItem;
import de.softwarekollektiv.dbs.app.AbstractSelectionMenu;

public class DbConnectionMenu extends AbstractSelectionMenu {

	private List<MenuItem> items;
	
	public DbConnectionMenu(PrintStream out, InputStream in, DbConnection dbcon) {
		super(out, in);
		
		items = new LinkedList<MenuItem>();
		items.add(new CredentialsDialog(out, in, dbcon));
		items.add(new QuitItem("Give up.", "Quit application."));
	}

	@Override
	protected List<MenuItem> getItems() {
		return items;
	}

	@Override
	protected String getGreeter() {
		return "The database connection could not be established. Would you like to provide credentials?";
	}

}
