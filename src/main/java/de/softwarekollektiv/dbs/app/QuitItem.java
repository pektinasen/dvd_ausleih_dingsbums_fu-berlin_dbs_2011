package de.softwarekollektiv.dbs.app;

public class QuitItem implements MenuItem {
	
	private final String title;
	private final String description;
	
	public QuitItem() {
		this("Quit", "Return from nested menu or quit application.");
	}
	
	public QuitItem(String title, String description) {
		this.title = title;
		this.description = description;
	}
	
	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public String getDescription() {
		return description;
	}

	@Override
	public boolean run() {
		return false;
	}
}
