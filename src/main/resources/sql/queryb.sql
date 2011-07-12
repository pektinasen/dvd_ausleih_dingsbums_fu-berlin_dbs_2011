-- commented out as postgres complains if already created and there's
-- no way of CREATing it IF NOT EXISTs...
-- CREATE LANGUAGE plpgsql;

-- this method should move to create.sql someday
CREATE OR REPLACE FUNCTION totalcharges(integer, integer, integer)
RETURNS numeric
AS
$$
	DECLARE
		v_rental RECORD;
		v_result NUMERIC DEFAULT 0.0;
		flat boolean;
		firstB boolean DEFAULT false;
		firstB_start date;
		firstB_duration numeric;
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
					WHERE rentals.cus_id = ' || pcus_id || ';';
			flat := false;
			FOR v_rental IN EXECUTE sqlexpr LOOP	
				IF v_rental.type = 'flat' THEN
					flat := true;
					IF firstB IS NULL AND v_rental.price_category = 'B' THEN
						firstB = true;
						firstB_start := v_rental.startdate;
						firstB_duration := v_rental.duration;
						raise notice '%',v_rental;
					END IF;
					v_result := v_result + v_rental.duration * 0.19;
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
			
			IF flat = true THEN			
				v_result := v_result + 10;
				IF firstB THEN
					SELECT MIN(startdate) INTO firstBEver 
					FROM v_rentals
					WHERE cus_id = pcus_id;
					
					IF firstB_start = firstBEver THEN
						v_result := v_result - firstB_duration * 0.19;
					END IF;
				END IF;
			END IF;
			
			RETURN v_result;
	END;
$$
LANGUAGE 'plpgsql' STABLE;

CREATE OR REPLACE FUNCTION getjanuaryreports()
RETURNS TABLE(cus_id integer, summe numeric)
AS
$$
	BEGIN
		RETURN QUERY SELECT rentals.cus_id, totalcharges(rentals.cus_id, 1, 2011) FROM rentals;
	END;
$$
LANGUAGE 'plpgsql' STABLE;

SELECT * FROM getjanuaryreports();
