package de.softwarekollektiv.dbs.app;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.List;

public abstract class AbstractSelectionMenu {
	protected PrintStream out;
	protected BufferedReader in;
	
	/**
	 * Get all selectable menu items. Must be implemented by subclasses.
	 * 
	 * @return a list of all menu items
	 */
	protected abstract List<MenuItem> getItems();
	
	/**
	 * Get the menu greeter. This should be a nice one-liner describing
	 * what this menu contains (e.g. category of menu items).
	 */
	protected abstract String getGreeter();
	
	public AbstractSelectionMenu(PrintStream out, InputStream in) {
		this.out = out;
		this.in = new BufferedReader(new InputStreamReader(in));
	}
	
	public boolean run() throws Exception {
		List<MenuItem> items = getItems();
		
		out.println();
		out.println(getGreeter());
		printItems();
		
		int choice = 0;
		while((choice < 1) || (choice > items.size())) {
			out.println("Please choose a number or enter '?' to read the descriptions: ");
			
			String line = in.readLine();
			
			if(line.equals("?")) {
				printDescriptions();
			} else {
				try {
					choice = Integer.parseInt(line);
				} catch (NumberFormatException e) {
					// Do nothing but let the user try again.
				}
			}
		}
		out.println();
		
		// Do NOT catch the exception.
		return items.get(choice - 1).run();
	}
	
	private void printItems() {
		List<MenuItem> items = getItems();
		for(int i = 0; i < items.size(); ++i) {
			out.print(i + 1);
			out.print(") ");
			out.println(items.get(i).getTitle());
		}
		out.println();
	}
	
	private void printDescriptions() {
		List<MenuItem> items = getItems();
		for(int i = 0; i < items.size(); ++i) {
			out.print(i + 1);
			out.print(") ");
			out.println(items.get(i).getTitle());
			out.println(items.get(i).getDescription());
			out.println();
		}
	}

}
