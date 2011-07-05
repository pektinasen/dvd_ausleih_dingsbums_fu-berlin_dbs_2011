DROP TABLE IF EXISTS movies CASCADE;
DROP TABLE IF EXISTS directors CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS actors CASCADE;
DROP TABLE IF EXISTS directedBy;
DROP TABLE IF EXISTS features;
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
	mov_id integer references movies,
	act_id integer references actors
);

CREATE TABLE directors (
	dir_id integer primary key,
	name varchar(64)
);

CREATE TABLE directedBy (
	dir_id integer references directors,
	mov_id integer references movies
);

CREATE TABLE locations (
	loc_id integer primary key,
	name varchar(64)
);

CREATE TABLE shotIn (
	mov_id integer references movies,
	loc_id integer references locations
);

CREATE TABLE customer (
	cus_id integer primary key,
	name varchar(64),
	surname varchar(64),
	street varchar(128),
	zip varchar(5),
	city varchar(64),
	phone varchar(32)
);
