SELECT
    to_char(scheduled_departure, 'Day') AS day_of_week,
    to_char(scheduled_departure, 'HH24:MI') AS departure_time,
    flight_no,
    arrival_airport AS destination
FROM flights
WHERE departure_airport = :airport_code
ORDER BY scheduled_departure