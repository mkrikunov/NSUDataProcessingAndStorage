SELECT
    to_char(scheduled_arrival, 'Day') AS day_of_week,
    to_char(scheduled_arrival, 'HH24:MI') AS arrival_time,
    flight_no,
    departure_airport AS origin
FROM flights
WHERE arrival_airport = :airport_code
ORDER BY scheduled_arrival