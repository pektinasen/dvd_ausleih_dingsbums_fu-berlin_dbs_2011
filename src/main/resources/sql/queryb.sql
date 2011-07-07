SELECT cus_id, SUM(summe) AS total FROM (

-- speedy, category A = 0.19/h
(SELECT cus_id, SUM(duration * 0.19) AS summe FROM rentals JOIN movies ON rentals.mov_id = movies.mov_id WHERE type = 'speedy' AND price_category = 'A'  AND EXTRACT(MONTH FROM startdate) = 1 GROUP BY cus_id)
UNION
-- speedy, category B = 0.15/h
(SELECT cus_id, SUM(duration * 0.15) AS summe FROM rentals JOIN movies ON rentals.mov_id = movies.mov_id WHERE type = 'speedy' AND price_category = 'B'  AND EXTRACT(MONTH FROM startdate) = 1 GROUP BY cus_id)
UNION
-- starter, category A = 1.29/d
(SELECT cus_id, SUM(duration * 1.29) AS summe FROM rentals JOIN movies ON rentals.mov_id = movies.mov_id WHERE type = 'starter' AND price_category = 'A'  AND EXTRACT(MONTH FROM startdate) = 1 GROUP BY cus_id)
UNION
-- starter, category B = 0.79/d
(SELECT cus_id, SUM(duration * 0.79) AS summe FROM rentals JOIN movies ON rentals.mov_id = movies.mov_id WHERE type = 'starter' AND price_category = 'B'  AND EXTRACT(MONTH FROM startdate) = 1 GROUP BY cus_id)
UNION
-- flat, category A = 0.19/d
(SELECT cus_id, SUM(duration * 0.19) AS summe FROM rentals JOIN movies ON rentals.mov_id = movies.mov_id WHERE type = 'flat' AND price_category = 'A' AND  EXTRACT(MONTH FROM startdate) = 1 GROUP BY cus_id)
UNION
-- flat, category B = 0.19/d + der erste Film gratis (-0.19)
(SELECT cus_id, (SUM(duration * 0.19) - 0.19) AS summe FROM rentals JOIN movies ON rentals.mov_id = movies.mov_id WHERE type = 'flat' AND price_category = 'B' AND EXTRACT(MONTH FROM startdate) = 1 GROUP BY cus_id)
UNION 
-- flat, 10.-/month
(SELECT cus_id, 10 AS summe FROM rentals WHERE type = 'flat' AND EXTRACT(MONTH FROM startdate) = 1 GROUP BY cus_id)

) AS tbl GROUP BY cus_id;
