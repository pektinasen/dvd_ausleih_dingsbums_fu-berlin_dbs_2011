--DROP TABLE IF EXISTS movies CASCADE;
DROP TABLE IF EXISTS actors;
DROP TABLE IF EXISTS directors;
DROP TABLE IF EXISTS locations;


/*CREATE TABLE movies(
	title varchar(128),
	release_date date,
	CHECK (release_date > '2010-01-01'),
	PRIMARY KEY (title, release_date)
);*/

CREATE TABLE actors (
	name varchar (64) primary key,
	title varchar(128),
	release_date date,
	FOREIGN KEY (title, release_date)
		REFERENCES movies(title, release_date) ON DELETE CASCADE
);
CREATE TABLE directors (
	name varchar (64) primary key,
	title varchar(128),
	release_date date,
	FOREIGN KEY (title, release_date)
		REFERENCES movies(title, release_date) ON DELETE CASCADE
);
CREATE TABLE locations (
	name varchar (64) primary key,
	title varchar(128),
	release_date date,
	FOREIGN KEY (title, release_date)
		REFERENCES movies(title, release_date) ON DELETE CASCADE
);
