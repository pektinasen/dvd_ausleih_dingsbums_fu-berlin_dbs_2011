package de.softwarekollektiv.dbs.model;

public class Actor {
	/*
	 * name is in the format: <lastname>, <firstname>
	 */
	private String name;

	/*
	 * foreign key on movies
	 */
	private Movie movie;

	public void setName(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}
	
	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Movie getMovie() {
		return movie;
	}
}
