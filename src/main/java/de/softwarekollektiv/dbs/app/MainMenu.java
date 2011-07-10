package de.softwarekollektiv.dbs.app;

import java.io.InputStream;
import java.io.PrintStream;
import java.sql.BatchUpdateException;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.dbcon.DbConnectionMenu;
import de.softwarekollektiv.dbs.invoices.InvoiceMenu;
import de.softwarekollektiv.dbs.parser.ParserCommander;
import de.softwarekollektiv.dbs.queries.listings.ListingsMenu;
import de.softwarekollektiv.dbs.queries.simple.SimpleQueryMenu;

class MainMenu extends AbstractSelectionMenu {
	private static final Logger log = Logger.getLogger(MainMenu.class);
	private final List<MenuItem> items;
		
	MainMenu(PrintStream out, InputStream in, DbConnection dbcon) throws Exception {
		super(out, in);
		
		items = new LinkedList<MenuItem>();
		items.add(new ParserCommander(dbcon));
		items.add(new ListingsMenu(out, in, dbcon));
		items.add(new InvoiceMenu(out, in, dbcon));
		items.add(new SimpleQueryMenu(out, in, dbcon));
		items.add(new QuitItem());
	}

	@Override
	protected List<MenuItem> getItems() {
		return items;
	}

	@Override
	protected String getGreeter() {
		return "Main menu";
	}
	
	/**
	 * main entrance point
	 */
	public static void main(String[] args) {
		DbConnection dbcon = null;
		PrintStream out = System.out;
		InputStream in = System.in;
		
		Logger.getRootLogger().setLevel(Level.DEBUG); // TODO remove later

		try {			
			dbcon = new DbConnection();
			while(!dbcon.openConnection()) {
				if(!(new DbConnectionMenu(out, in, dbcon)).run())
					return;
			}
			
			MainMenu mainMenu = new MainMenu(out, in, dbcon);
			mainMenu.run();
			
			dbcon.closeConnection();
		} catch (Exception e) {
			// Any severe exception (e.g. IOException on console) ends up here.
			log.fatal("Fatal error: ", e);
			
			if(e instanceof BatchUpdateException) {
				BatchUpdateException bue = (BatchUpdateException) e;
				log.fatal("Next Exception: ", bue.getNextException());
			}
			
			if(dbcon != null)
				dbcon.closeConnection();
			System.exit(1);
		}
	}
}
