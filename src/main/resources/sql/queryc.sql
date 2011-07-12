SELECT nomi.monat, (CAST(nomi.nomic AS numeric) / CAST(nonnomi.nonnomic AS numeric)) AS ratio FROM
(SELECT EXTRACT(MONTH FROM m1.release_date) AS monat, COUNT(*) AS nomic FROM nominations n JOIN movies m1 ON n.mov_id = m1.mov_id GROUP BY EXTRACT(MONTH FROM m1.release_date)) nomi
JOIN
(SELECT EXTRACT(MONTH FROM m2.release_date) AS monat, COUNT(*) AS nonnomic FROM movies m2 GROUP BY EXTRACT(MONTH FROM m2.release_date)) nonnomi
ON nomi.monat = nonnomi.monat ORDER BY nomi.monat;


