package de.softwarekollektiv.dbs.app;

public class QuitItem implements MenuItem {
	@Override
	public String getTitle() {
		return "Quit";
	}

	@Override
	public String getDescription() {
		return "Return from nested menu or quit application.";
	}

	@Override
	public boolean run() {
		return false;
	}
}
