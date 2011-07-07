SELECT cus_id, COUNT(*) as num FROM rentals r WHERE r.mov_id IN (SELECT DISTINCT mov_id FROM shotIn s JOIN locations l ON s.loc_id = l.loc_id WHERE l.country <> 'USA') GROUP BY r.cus_id
