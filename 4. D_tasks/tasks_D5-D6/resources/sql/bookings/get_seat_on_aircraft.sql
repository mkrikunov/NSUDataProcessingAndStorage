SELECT seat_no
FROM seats
WHERE aircraft_code = (SELECT aircraft_code FROM flights WHERE flight_id = :flight_id)
  AND seat_no NOT IN (
  SELECT seat_no FROM boarding_passes WHERE flight_id = :flight_id
                                      )
AND fare_conditions='Economy'
LIMIT 1