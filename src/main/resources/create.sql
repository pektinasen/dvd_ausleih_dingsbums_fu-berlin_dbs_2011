DROP TABLE IF EXISTS movies CASCADE;
DROP TABLE IF EXISTS actors;
DROP TABLE IF EXISTS features;
DROP TABLE IF EXISTS directors;
DROP TABLE IF EXISTS directedBy;
DROP TABLE IF EXISTS locations;
DROP TABLE IF EXISTS shotIn;

CREATE TABLE movies (
	mov_id integer primary key,
	title text,
	release_date date,
	CHECK (release_date >= '2010-01-01')
);

CREATE TABLE actors (
	act_id integer primary key,
	name varchar(64),
	male boolean
);

CREATE TABLE features (
	mov_id references movies,
	act_id references actors
);

CREATE TABLE directors (
	dir_id integer primary key,
	name varchar(64)
);

CREATE TABLE directedBy (
	dir_id references directors,
	mov_id references movies
);

CREATE TABLE locations (
	loc_id integer primary key,
	name varchar(64)
);

CREATE TABLE shotIn (
	mov_id references movies,
	loc_id references locations
);
