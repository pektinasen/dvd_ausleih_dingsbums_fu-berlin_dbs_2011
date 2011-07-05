package de.softwarekollektiv.dbs.app;

import java.io.InputStream;
import java.io.PrintStream;
import java.util.LinkedList;
import java.util.List;

import de.softwarekollektiv.dbs.Main;

public class MainMenu extends SelectionMenu {
	
	private List<MenuItem> items;
	
	/**
	 * main entrance point
	 */
	public static void main(String[] args) throws Exception {
		(new MainMenu(System.out, System.in)).run();
	}
	
	public MainMenu(PrintStream out, InputStream in) {
		super(out, in);
		
		items = new LinkedList<MenuItem>();
		
		items.add(new Main());
		items.add(new QuitItem());
	}

	@Override
	public String getTitle() {
		// unused
		return "Main menu";
	}

	@Override
	public String getDescription() {
		// unused
		return "Main menu";
	}

	@Override
	protected List<MenuItem> getItems() {
		return items;
	}
}
