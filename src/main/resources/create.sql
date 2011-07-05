DROP TABLE IF EXISTS movies CASCADE;
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
	release_date date,
	CHECK (release_date >= '2010-01-01')
);

CREATE TABLE actors (
	act_id serial primary key,
	name varchar (64)
	
);
CREATE TABLE directors (
	dir_id serial  primary key,
	name varchar (64)
);
CREATE TABLE locations (
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
