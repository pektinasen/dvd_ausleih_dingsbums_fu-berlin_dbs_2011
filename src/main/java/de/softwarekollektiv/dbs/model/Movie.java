package de.softwarekollektiv.dbs.model;


import java.sql.Date;
import java.util.Calendar;

import org.apache.log4j.Logger;

import de.softwarekollektiv.dbs.orm.Entity;

public class Movie extends Entity{
	
	
	private int id;
	
	private String title;
	private Date releaseDate;

	public Logger log = Logger.getLogger(Movie.class);

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

	/*
	 * TODO The output for the String "2010" is '2010-07-03'
	 */
	public void setReleaseDate(String releaseDate) {
		
	}

	public Date getReleaseDate() {
		return releaseDate;
	}
}
