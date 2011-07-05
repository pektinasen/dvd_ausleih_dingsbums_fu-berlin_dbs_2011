package de.softwarekollektiv.dbs.app;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.dbcon.DbConnection;
import de.softwarekollektiv.dbs.dbcon.DbConnectionMenu;
import de.softwarekollektiv.dbs.parser.ParserCommander;

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
			// Every severe exception (e.g. IOException on console) ends up here.
			Logger.getRootLogger().fatal("Fatal error: ", e);
			System.exit(1);
		}
	}
	
	public Main(PrintStream out, InputStream in) throws Exception {
		super(out, in);
		
		dbcon = new DbConnection();
		boolean tryToConnect = true;
		while(tryToConnect && !dbcon.openConnection()) {
			tryToConnect = new DbConnectionMenu(out, in, dbcon).run();
		}
		
		// Did the user want to abort?
		if(!tryToConnect)
			return;
		
		items = new LinkedList<MenuItem>();
		items.add(new ParserCommander(dbcon));
		items.add(new QuitItem());
		
		while(super.run())
			;
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
