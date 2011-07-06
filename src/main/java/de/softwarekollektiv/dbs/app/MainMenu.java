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

public class MainMenu extends AbstractSelectionMenu {
	
	private List<MenuItem> items;
		
	public MainMenu(PrintStream out, InputStream in, DbConnection dbcon) throws Exception {
		super(out, in);
		
		items = new LinkedList<MenuItem>();
		items.add(new ParserCommander(dbcon));
		items.add(new ListingsMenu(out, in, dbcon));
		items.add(new InvoiceMenu(out, in, dbcon));
		items.add(new QueryMenu(out, in, dbcon));
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
		try {
			PrintStream out = System.out;
			InputStream in = System.in;
			
			DbConnection dbcon = new DbConnection();
			while(!dbcon.openConnection()) {
				if(!(new DbConnectionMenu(out, in, dbcon)).run())
					return;
			}
			
			MainMenu mainMenu = new MainMenu(out, in, dbcon);
			mainMenu.run();
			
			dbcon.closeConnection();
		} catch (Exception e) {
			// Any severe exception (e.g. IOException on console) ends up here.
			Logger.getRootLogger().fatal("Fatal error: ", e);
			e.printStackTrace();
			System.exit(1);
		}
	}
}
