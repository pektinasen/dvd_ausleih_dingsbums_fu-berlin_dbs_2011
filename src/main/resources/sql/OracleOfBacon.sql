/*
 * Angewendet wird eine rekursive Breitensuche
 */
--DROP FUNCTION baconNumber(varchar(128), varchar(128));
CREATE TABLE visited (
	id serial,
	val varchar(128) primary key
);

CREATE TABLE queue (
	id serial,
	val varchar(128) primary key
);
--DROP FUNCTION baconNumber(varchar(128), varchar(128));
CREATE OR REPLACE FUNCTION baconNumber(varchar(128), varchar(128))
RETURNS /*SETOF*/ varchar(128)
AS
$BODY$
DECLARE
	i int;
	v_todo text[];
	v_node text;
	v_neighbor text;
	v_act_id int;
	v_count int;
	q_count int;
	p_from ALIAS FOR $1;
	p_to ALIAS FOR $2;
	
BEGIN
	-- start with p_from
	EXECUTE 'INSERT INTO queue VALUES (DEFAULT, '||quote_literal(p_from)||');';
	i := 1;
	-- start node is now visited
	EXECUTE 'INSERT INTO visited VALUES (DEFAULT, '||quote_literal(p_from)||');';
	-- while
	LOOP
		i := i + 1;
		-- so lange queue nicht leer ist...
		SELECT count(*) from queue INTO q_count;
		EXIT WHEN q_count = 0;
		
		-- nimm erstes element 
		SELECT val from queue WHERE id = (SELECT MIN(id) FROM queue) INTO v_node;
		IF NOT FOUND THEN 
			EXIT; 
		END IF;
		-- soll eine art pop operation auf die queue sein
		DELETE FROM queue where val = v_node;
		raise notice '%',v_node;
		-- haben wir unser Ziel schon gefunden?
		IF v_node = p_to THEN
			RETURN i;
		END IF;
		
		FOR v_neighbor IN SELECT neighborsOf(v_node) LOOP
			SELECT count(*) from visited where val = v_neighbor INTO v_count;
			IF v_count = 0 THEN
				EXECUTE 'INSERT INTO queue VALUES (DEFAULT, '||quote_literal(v_neighbor)||');';
				EXECUTE 'INSERT INTO visited VALUES (DEFAULT, '||quote_literal(v_neighbor)||');';
			END IF;
		END LOOP;
	END LOOP;
	-- while end
	
	RETURN 'false';
END
$BODY$ LANGUAGE plpgsql;


CREATE OR REPLACE FUNCTION neighborsOf(varchar(128))
RETURNS SETOF varchar(128)
AS
$BODY$
DECLARE
	v_sql_expr text;
	p_source ALIAS FOR $1;
BEGIN
	v_sql_expr := 'SELECT name
			FROM features JOIN actors USING (act_id)
			WHERE mov_id IN (
				SELECT DISTINCT mov_id
				FROM features JOIN actors USING (act_id)
				WHERE name = '|| quote_literal(p_source) ||'
			) 
			EXCEPT
			SELECT name
			FROM actors
			WHERE name = '|| quote_literal(p_source) || ';';
			
	RETURN QUERY EXECUTE v_sql_expr;
END
$BODY$ LANGUAGE plpgsql;

Select * from baconNumber('Depp, Johnny', 'Dalton, Timothy');

DROP TABLE visited;
DROP TABLE queue;