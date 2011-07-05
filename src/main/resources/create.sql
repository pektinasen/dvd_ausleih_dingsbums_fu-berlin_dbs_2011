DROP TABLE IF EXISTS movies CASCADE;
<<<<<<< HEAD
DROP TABLE IF EXISTS actors CASCADE;
DROP TABLE IF EXISTS directors CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS customers CASCADE;
DROP TABLE IF EXISTS plays_in CASCADE;
DROP TABLE IF EXISTS directs CASCADE;
DROP TABLE IF EXISTS shot_in CASCADE;


CREATE TABLE movies(
	mov_id serial primary key,
	title varchar(128),
=======
DROP TABLE IF EXISTS directors CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS actors CASCADE;
DROP TABLE IF EXISTS directedBy;
DROP TABLE IF EXISTS features;
DROP TABLE IF EXISTS shotIn;

CREATE TABLE movies (
	mov_id integer primary key,
	title text,
>>>>>>> 02a008d60f5c890114d97517a77ec96b835249ba
	release_date date,
	CHECK (release_date >= '2010-01-01')
);

CREATE TABLE actors (
<<<<<<< HEAD
	act_id serial primary key,
	name varchar (64)
	
=======
	act_id integer primary key,
	name varchar(64),
	male boolean
>>>>>>> 02a008d60f5c890114d97517a77ec96b835249ba
);

CREATE TABLE features (
	mov_id integer references movies,
	act_id integer references actors
);

CREATE TABLE directors (
<<<<<<< HEAD
	dir_id serial  primary key,
	name varchar (64)
=======
	dir_id integer primary key,
	name varchar(64)
>>>>>>> 02a008d60f5c890114d97517a77ec96b835249ba
);

CREATE TABLE directedBy (
	dir_id integer references directors,
	mov_id integer references movies
);

CREATE TABLE locations (
<<<<<<< HEAD
	loc_id serial primary key,
	name varchar (64)
);

CREATE TABLE plays_in (
	act_id integer references actors,
	mov_id integer references movies
);

CREATE TABLE directs (
	dir_id integer references directors,
	mov_id integer references movies
);

Create TABLE shot_in (
	loc_id integer references locations,
	mov_id integer references movies
);

CREATE TABLE customers (
	cus_id serial primary key,
	surname varchar(128),
	forename varchar(128),
	street varchar(128),
	zip varchar(5),
	city varchar(32),
	phone varchar(32)
);

/*INSERT INTO movies VALUES (DEFAULT, 'Fluch der Karibik', '2010-01-01');
INSERT INTO actors VALUES (DEFAULT, 'Johnny Depp');
INSERT INTO plays_in VALUES ((SELECT act_id FROM actors WHERE name = 'Johnny Depp'), (SELECT mov_id FROM movies WHERE title = 'Fluch der Karibik'));
*/
=======
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
>>>>>>> 02a008d60f5c890114d97517a77ec96b835249ba
