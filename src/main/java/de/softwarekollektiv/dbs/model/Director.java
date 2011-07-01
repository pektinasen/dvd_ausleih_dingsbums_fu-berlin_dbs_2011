package de.softwarekollektiv.dbs.model;

public class Director {

	private String name;
	
	/*
	 * foreign key on movies
	 */
	private int movie;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setMovie(int movie) {
		this.movie = movie;
	}

	public int getMovie() {
		return movie;
	}
}
