package de.softwarekollektiv.dbs.model;

public class Actor {
	/*
	 * name is in the format: <lastname>, <firstname>
	 */
	private String name;
	private SEX sex;
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

	public void setSex(SEX sex) {
		this.sex = sex;
	}

	public SEX getSex() {
		return sex;
	}

	public void setMovie(Movie movie) {
		this.movie = movie;
	}

	public Movie getMovie() {
		return movie;
	}
}
