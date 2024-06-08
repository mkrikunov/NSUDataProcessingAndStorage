SELECT COALESCE(MAX(boarding_no), 0) + 1 AS new_boarding_no
FROM boarding_passes
WHERE flight_id = :flight_id