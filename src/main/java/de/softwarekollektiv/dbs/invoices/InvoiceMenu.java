package de.softwarekollektiv.dbs.invoices;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import de.softwarekollektiv.dbs.app.MenuItem;
import de.softwarekollektiv.dbs.app.QuitItem;
import de.softwarekollektiv.dbs.app.AbstractSelectionMenu;
import de.softwarekollektiv.dbs.dbcon.DbConnection;

public class InvoiceMenu extends AbstractSelectionMenu implements MenuItem {

	private List<MenuItem> items;
	
	public InvoiceMenu(PrintStream out, InputStream in, DbConnection dbcon) {
		super(out, in);
		
		items = new LinkedList<MenuItem>();
		items.add(new SingleInvoice(out, in, dbcon));
		items.add(new QuitItem("Return", "Return to main menu."));
	}

	@Override
	public String getTitle() {
		return "Invoices";
	}

	@Override
	public String getDescription() {
		return "Generate invoices for each rental or accumulated by user per year";
	}

	@Override
	protected List<MenuItem> getItems() {
		return items;
	}

	@Override
	protected String getGreeter() {
		return "Invoices";
	}

}
