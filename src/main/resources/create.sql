DROP TABLE IF EXISTS movies CASCADE;
DROP TABLE IF EXISTS actors;
DROP TABLE IF EXISTS directors;
DROP TABLE IF EXISTS locations;


CREATE TABLE movies(
	mov_id integer primary key,
	title varchar(128),
	release_date date,
	CHECK (release_date >= '2010-01-01'),

);

CREATE TABLE actors (
	act_id integer primary key,
	name varchar (64),
	mov_id references movie
);
CREATE TABLE directors (
	dir_id integer  primary key,
	name varchar (64),
	mov_id references movie
);
CREATE TABLE locations (
	loc_id integer primary key,
	name varchar (64),
	mov_id integer references movie
);

CREATE TABLE customers (
	cus_id integer primary key,
	surname varchar(128),
	forename varchar(128),
	street varchar(128),
	phone varchar(32)
);

