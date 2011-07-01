package de.softwarekollektiv.dbs.model;

public class Location {

	private String name;
	
	/*
	 * foreign key on movies
	 */
	private int movie;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setMovie(int movie) {
		this.movie = movie;
	}

	public int getMovie() {
		return movie;
	}
	
}
