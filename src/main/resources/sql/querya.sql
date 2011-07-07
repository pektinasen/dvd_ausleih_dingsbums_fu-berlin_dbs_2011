SELECT type, COUNT(DISTINCT cus_id) AS num FROM rentals GROUP BY type
