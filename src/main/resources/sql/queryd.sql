SELECT t1.cus_id, (CAST(t1.foreign AS numeric) / CAST(t2.total AS numeric)) AS ratio FROM
(SELECT r.cus_id, COUNT(*) as foreign FROM rentals r WHERE r.mov_id IN (SELECT DISTINCT mov_id FROM shotIn s JOIN locations l ON s.loc_id = l.loc_id WHERE l.country <> 'USA') GROUP BY r.cus_id) t1
JOIN
(SELECT r.cus_id, COUNT(*) as total FROM rentals r GROUP BY r.cus_id) t2
ON t1.cus_id = t2.cus_id ORDER BY ratio DESC;
