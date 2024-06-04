SELECT DISTINCT city FROM (
    SELECT departure_airport AS airport_code FROM flights
    UNION
    SELECT arrival_airport AS airport_code FROM flights
) AS flight_airports
JOIN airports ON flight_airports.airport_code = airports.airport_code;