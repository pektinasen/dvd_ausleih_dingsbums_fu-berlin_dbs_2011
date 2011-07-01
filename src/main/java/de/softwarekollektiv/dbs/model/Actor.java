package de.softwarekollektiv.dbs.model;

public class Actor {
	private String name;
	private SEX sex;
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

	public void setSex(SEX sex) {
		this.sex = sex;
	}

	public SEX getSex() {
		return sex;
	}

	public void setMovie(int movie) {
		this.movie = movie;
	}

	public int getMovie() {
		return movie;
	}
}
