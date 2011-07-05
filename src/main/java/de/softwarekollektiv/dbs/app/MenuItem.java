package de.softwarekollektiv.dbs.app;

public interface MenuItem {
	
	/**
	 * @return the title of the menu item 
	 */
	public String getTitle();
	
	/**
	 * @return a short description of what this menu item does
	 */
	public String getDescription();
	
	/**
	 * Execute this menu item's action.
	 * In case <c>false</c> is returned, the higher-level menu should abort.
	 * 
	 * @return true on success, fail if the application should quit.
	 * @throws Exception if any exception is thrown from this method, the application
	 * 			should quit immediately.
	 */
	public boolean run() throws Exception;
}
