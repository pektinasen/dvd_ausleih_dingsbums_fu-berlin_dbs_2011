package de.softwarekollektiv.dbs.app;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import de.softwarekollektiv.dbs.DbConnection;
import de.softwarekollektiv.dbs.parser.ParserCommander;

public class Main extends SelectionMenu {
	
	private List<MenuItem> items;
	private DbConnection dbcon;
	
	/**
	 * main entrance point
	 */
	public static void main(String[] args) throws Exception {
		new Main(System.out, System.in);
	}
	
	public Main(PrintStream out, InputStream in) throws Exception {
		super(out, in);
		
		dbcon = new DbConnection();
		// Try to open the connection
		
		// Ask for credentials
		
		items = new LinkedList<MenuItem>();
		items.add(new ParserCommander(dbcon));
		items.add(new QuitItem());
		
		super.run();
	}

	@Override
	public String getTitle() {
		// unused
		return "ParserCommander menu";
	}

	@Override
	public String getDescription() {
		// unused
		return "ParserCommander menu";
	}

	@Override
	protected List<MenuItem> getItems() {
		return items;
	}
}
