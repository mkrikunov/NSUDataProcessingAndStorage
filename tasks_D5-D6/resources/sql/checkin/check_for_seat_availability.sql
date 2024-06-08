SELECT *
FROM seats
WHERE aircraft_code = :aircraft_code AND seat_no = :seat_no AND fare_conditions = (
    SELECT fare_conditions FROM ticket_flights WHERE ticket_no = :ticket_no AND flight_id = :flight_id
)