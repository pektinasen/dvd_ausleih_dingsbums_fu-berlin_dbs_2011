-- commented out as postgres complains if already created and there's
-- no way of CREATing it IF NOT EXISTs...
-- CREATE LANGUAGE plpgsql;

-- this method should move to create.sql someday
CREATE OR REPLACE FUNCTION totalcharges(integer, integer, integer)
RETURNS integer
AS
$$
	DECLARE
		rental RECORD;
		result NUMERIC;
		flat boolean;
		firstB RECORD;
		firstBEver timestamp;
		sqlexpr text;
		pcus_id ALIAS FOR $1;
		pmonth ALIAS FOR $2;
		pyear ALIAS FOR $3;
	BEGIN
			/* TODO s/560'/' || CAST(pcus_id AS text)/ */
			sqlexpr := 'SELECT price_category, type, startdate, duration
					FROM rentals JOIN movies
					ON rentals.mov_id = movies.mov_id
					WHERE rentals.cus_id = 560';
			flat := false;
			FOR rental IN EXECUTE sqlexpr LOOP
				IF rental.type = 'flat' THEN
					flat := true;
					IF firstB IS NULL AND rental.price_category = 'B' THEN
						firstB = rental;
					END IF;
					result := result + rental.duration * 0.19;	
				ELSE 
					IF rental.type = 'speedy' THEN
						IF rental.price_category = 'A' THEN
							result := result + rental.duration * 0.19;
						ELSE
							result := result + rental.duration * 0.15;
						END IF;
					ELSE
						IF rental.price_category = 'A' THEN
							result := result + rental.duration * 1.29;
						ELSE
							result := result + rental.duration * 0.79;
						END IF;
					END IF;
				END IF;
			END LOOP;
			
			IF flat = true THEN
				result := result + 10;
				IF firstB <> NULL THEN
					SELECT MIN(startdate) INTO firstBEver 
					FROM rentals
					WHERE cus_id = pcus_id;
					
					IF firstB.starttime = firstBEver THEN
						result := result - firstB.duration * 0.19;
					END IF;
				END IF;
			END IF;
			
			RETURN result;
	END;
$$
LANGUAGE 'plpgsql' STABLE;


CREATE OR REPLACE FUNCTION getjanuaryreports()
RETURNS TABLE(cus_id integer, summe integer)
AS
$$
	DECLARE
		result record;
	BEGIN
		FOR result IN SELECT cus_id, totalcharges(cus_id, 1, 2011) FROM rentals LOOP
			RETURN NEXT;
		END LOOP;
		RETURN;
	END;
$$
LANGUAGE 'plpgsql' STABLE;

SELECT * FROM getjanuaryreports();
