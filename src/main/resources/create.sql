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
	mov_id serial primary key,
	title text unique,
	release_date date,
	description text,
	price_category char,
	region varchar(2),
	CHECK (release_date >= '2010-01-01')
);

CREATE TABLE actors (
	act_id serial primary key,
	name varchar(64),
	male boolean
);

CREATE TABLE features (
	mov_id integer references movies,
	act_id integer references actors,
	primary key (mov_id, act_id)
);

CREATE TABLE directors (
	dir_id serial primary key,
	name varchar(64)
);

CREATE TABLE directedBy (
	mov_id integer references movies,
	dir_id integer references directors,
	primary key (mov_id, dir_id)
);

CREATE TABLE locations (
	loc_id serial primary key,
	name varchar(64),
	country varchar(64)
);

CREATE TABLE shotIn (
	mov_id integer references movies,
	loc_id integer references locations,
	primary key (mov_id, loc_id)
);

CREATE TABLE customers (
	cus_id integer primary key,
	surename varchar(64),
	forename varchar(64),
	street varchar(128),
	zip varchar(5),
	city varchar(64),
	phone varchar(32)
);

CREATE TABLE rentals (
	cus_id integer references customers,
	type varchar(7),
	mov_id integer references movies,
	startdate timestamp,
	duration integer
);
