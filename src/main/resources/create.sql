DROP TABLE IF EXISTS movies CASCADE;
DROP TABLE IF EXISTS directors CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS actors CASCADE;
DROP TABLE IF EXISTS directedBy;
DROP TABLE IF EXISTS features;
DROP TABLE IF EXISTS shotIn;
DROP TABLE IF EXISTS customers;
DROP TABLE IF EXISTS rentals;

CREATE TABLE movies (
	mov_id serial primary key,
	title text unique,
	release_date date,
	description text,
	price_category char
	CHECK (release_date >= '2010-01-01')
);

CREATE TABLE actors (
	act_id serial primary key,
	name varchar(64),
	male boolean
);

CREATE TABLE features (
	mov_id integer references movies,
	act_id integer references actors
);

CREATE TABLE directors (
	dir_id serial primary key,
	name varchar(64)
);

CREATE TABLE directedBy (
	dir_id integer references directors,
	mov_id integer references movies
);

CREATE TABLE locations (
	loc_id serial primary key,
	name varchar(64)
);

CREATE TABLE shotIn (
	mov_id integer references movies,
	loc_id integer references locations
);

CREATE TABLE customers (
	cus_id integer primary key,
	name varchar(64),
	surname varchar(64),
	street varchar(128),
	zip varchar(5),
	city varchar(64),
	phone varchar(32)
);

CREATE TABLE rentals (
	cus_id integer references customers,
	type varchar(7),
	mov_id integer references movies,
	startdate date,
	duration integer
);
