package de.softwarekollektiv.dbs.app;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.dbcon.DbConnectionMenu;
import de.softwarekollektiv.dbs.invoices.InvoiceMenu;
import de.softwarekollektiv.dbs.listings.ListingsMenu;
import de.softwarekollektiv.dbs.parser.ParserCommander;
import de.softwarekollektiv.dbs.queries.QueryMenu;

public class Main extends SelectionMenu {
	
	private List<MenuItem> items;
	private DbConnection dbcon;
	
	/**
	 * main entrance point
	 */
	public static void main(String[] args) {
		try {
			new Main(System.out, System.in);
		} catch (Exception e) {
			// Any severe exception (e.g. IOException on console) ends up here.
			Logger.getRootLogger().fatal("Fatal error: ", e);
			e.printStackTrace();
			System.exit(1);
		}
	}
	
	public Main(PrintStream out, InputStream in) throws Exception {
		super(out, in);
		
		dbcon = new DbConnection();
		while(!dbcon.openConnection()) {
			if(!(new DbConnectionMenu(out, in, dbcon)).run())
				return;
		}
		
		
		items = new LinkedList<MenuItem>();
		items.add(new ParserCommander(dbcon));
		items.add(new ListingsMenu(out, in, dbcon));
		items.add(new InvoiceMenu(out, in, dbcon));
		items.add(new QueryMenu(out, in, dbcon));
		items.add(new QuitItem());
		
		// Run main menu forever
		while(super.run())
			;
		
		dbcon.closeConnection();
	}

	@Override
	protected List<MenuItem> getItems() {
		return items;
	}

	@Override
	protected String getGreeter() {
		return "Main menu";
	}
}
