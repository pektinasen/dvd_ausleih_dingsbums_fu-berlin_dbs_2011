package de.softwarekollektiv.dbs.model;

import de.softwarekollektiv.dbs.orm.Entity;

public class Movie extends Entity{
	private int id;
	
	private String title;
	private int releaseDate;

	public void setId(int id) {
		this.id = id;
	}

	public int getId() {
		return id;
	}

	public void setTitle(String title) {
		this.title = title.replace("'", "''");
	}

	public String getTitle() {
		return title;
	}

	public void setReleaseDate(String releaseDate) {
		this.releaseDate = Integer.parseInt(releaseDate.split("-")[0]);
	}

	public int getReleaseDate() {
		return releaseDate;
	}
}
