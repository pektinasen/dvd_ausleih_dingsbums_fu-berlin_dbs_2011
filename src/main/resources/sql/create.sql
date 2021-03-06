DROP TABLE IF EXISTS movies CASCADE;
DROP TABLE IF EXISTS directors CASCADE;
DROP TABLE IF EXISTS locations CASCADE;
DROP TABLE IF EXISTS actors CASCADE;
DROP TABLE IF EXISTS directedBy;
DROP TABLE IF EXISTS features;
DROP TABLE IF EXISTS shotIn;
DROP TABLE IF EXISTS customers CASCADE;
DROP TABLE IF EXISTS rentals;
DROP TABLE IF EXISTS nominations;
DROP INDEX IF EXISTS act_idx;

CREATE TABLE movies (
	mov_id integer PRIMARY KEY,
	title text UNIQUE,
	release_date date,
	description text,
	price_category char,
	region varchar(64)
	-- CHECK (release_date >= '2010-01-01')
	-- removed as we also add some movies from 09 to the db (oscar nominees)
);

CREATE TABLE actors (
	act_id integer PRIMARY KEY,
	name varchar(128), 
	male boolean
	-- not unique because some actors appears twice in actors.list
);

CREATE TABLE features (
	mov_id integer REFERENCES movies,
	act_id integer REFERENCES actors,
	PRIMARY KEY (mov_id, act_id)
);

CREATE TABLE locations (
	loc_id integer PRIMARY KEY,
	name text UNIQUE,
	country varchar(64)
);

CREATE TABLE shotIn (
	mov_id integer REFERENCES movies,
	loc_id integer REFERENCES locations,
	PRIMARY KEY (mov_id, loc_id)
);

CREATE TABLE directors (
	dir_id integer PRIMARY KEY,
	name varchar(128)
);

CREATE TABLE directedBy (
	mov_id integer REFERENCES movies,
	dir_id integer REFERENCES directors,
	PRIMARY KEY (mov_id, dir_id)
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

CREATE TABLE nominations (
	mov_id integer REFERENCES movies,
	category varchar(8),
	year integer
);

/*
 * INDEXES
 */

CREATE INDEX act_idx ON features(act_id);

/*
 * STORED PROCEDURES
 */
 
-- commented out as postgres complains if already created and there's
-- no way of CREATing it IF NOT EXISTs...
-- CREATE LANGUAGE plpgsql;

-- Calculate charges for a customer (first parameter).
-- Set second parameter to [1..12] to restrict to a month, else set <0
-- Set third parameter to year to restrict to a year, else set <0
CREATE OR REPLACE FUNCTION totalcharges(integer, integer, integer)
RETURNS numeric
AS
$$
	DECLARE
		v_rental RECORD;
		v_result NUMERIC;
		firstBFlat boolean;
		firstB_start date;
		firstB_duration numeric;
		firstBEver timestamp;
		lastBRentalTS timestamp;
		sqlexpr text;
		pcus_id ALIAS FOR $1;
		pmonth ALIAS FOR $2;
		pyear ALIAS FOR $3;
	BEGIN
		sqlexpr := 'SELECT price_category, type, startdate, duration
				FROM rentals JOIN movies
				ON rentals.mov_id = movies.mov_id
				WHERE rentals.cus_id = ' || pcus_id;
		IF pmonth > 0 THEN 
				sqlexpr := sqlexpr || ' AND EXTRACT(MONTH FROM startdate) = ' || pmonth;
		END IF;
		IF pyear > 0 THEN 
				sqlexpr := sqlexpr || ' AND EXTRACT(YEAR FROM startdate) = ' || pyear;
		END IF;
		sqlexpr := sqlexpr || ' ORDER BY startdate;';
		
		firstBFlat := false;
		v_result := 0.0;
		lastBRentalTS := NULL;
		
		FOR v_rental IN EXECUTE sqlexpr LOOP	
			IF v_rental.type = 'flat' THEN
				IF firstBFlat = false AND v_rental.price_category = 'B' THEN
					firstBFlat := true;
					firstB_start := v_rental.startdate;
					firstB_duration := v_rental.duration;
				END IF;
				v_result := v_result + v_rental.duration * 0.19;

				IF lastBRentalTS IS NULL OR
					EXTRACT(MONTH FROM lastBRentalTS) <> EXTRACT(MONTH FROM v_rental.startdate) OR
					EXTRACT(YEAR FROM lastBRentalTS) <> EXTRACT(YEAR FROM v_rental.startdate) THEN
					
					lastBRentalTS := v_rental.startdate;
					v_result := v_result + 10.0;
					
				END IF;
			ELSE 
				IF v_rental.type = 'speedy' THEN
					IF v_rental.price_category = 'A' THEN
						v_result := v_result + v_rental.duration * 0.19;
					ELSE
						v_result := v_result + v_rental.duration * 0.15;
					END IF;
				ELSE
					IF v_rental.price_category = 'A' THEN
						v_result := v_result + v_rental.duration * 1.29;
					ELSE
						v_result := v_result + v_rental.duration * 0.79;
					END IF;
				END IF;
			END IF;
		END LOOP;
		
		IF firstBFlat THEN
			SELECT MIN(startdate) INTO firstBEver 
			FROM rentals
			WHERE cus_id = pcus_id;
			
			IF firstB_start = firstBEver THEN
				v_result := v_result - firstB_duration * 0.19;
			END IF;
		END IF;
		
		RETURN v_result;
	END;
$$
LANGUAGE 'plpgsql' STABLE;

CREATE OR REPLACE FUNCTION totalcharges(integer)
RETURNS numeric
AS
$$
	BEGIN
		RETURN totalcharges($1, -1,-1);
	END
$$ LANGUAGE 'plpgsql' STABLE;
