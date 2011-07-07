DROP TABLE IF EXISTS movies CASCADE;
DROP TABLE IF EXISTS directors CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS actors CASCADE;
DROP TABLE IF EXISTS directedBy;
DROP TABLE IF EXISTS features;
DROP TABLE IF EXISTS shotIn;
DROP TABLE IF EXISTS customers CASCADE;
DROP TABLE IF EXISTS rentals;

CREATE TABLE movies (
	mov_id serial PRIMARY KEY,
	title text UNIQUE,
	release_date date,
	description text,
	price_category char,
	region varchar(64),
	CHECK (release_date >= '2010-01-01')
);

CREATE TABLE actors (
	act_id serial PRIMARY KEY,
	name varchar(128), 
	male boolean, 
	UNIQUE (name, male)
);

CREATE TABLE features (
	mov_id integer REFERENCES movies,
	act_id integer REFERENCES actors,
	PRIMARY KEY (mov_id, act_id)
);

CREATE TABLE directors (
	dir_id serial PRIMARY KEY,
	name varchar(128) UNIQUE
);

CREATE TABLE directedBy (
	mov_id integer REFERENCES movies,
	dir_id integer REFERENCES directors,
	PRIMARY KEY (mov_id, dir_id)
);

CREATE TABLE locations (
	loc_id serial PRIMARY KEY,
	name text UNIQUE,
	country varchar(64)
);

CREATE TABLE shotIn (
	mov_id integer REFERENCES movies,
	loc_id integer REFERENCES locations,
	PRIMARY KEY (mov_id, loc_id)
);

CREATE TABLE customers (
	cus_id integer PRIMARY KEY,
	surename varchar(64),
	forename varchar(64),
	street varchar(128),
	zip varchar(5),
	city varchar(64),
	phone varchar(32)
);

CREATE TABLE rentals (
	cus_id integer REFERENCES customers,
	type varchar(7),
	mov_id integer REFERENCES movies,
	startdate timestamp,
	duration integer,
	PRIMARY KEY (cus_id, mov_id, startdate)
);
