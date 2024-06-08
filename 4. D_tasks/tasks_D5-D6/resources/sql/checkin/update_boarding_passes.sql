INSERT INTO boarding_passes (ticket_no, flight_id, boarding_no, seat_no)
VALUES (:ticket_no, :flight_id,
        (
        SELECT COALESCE(MAX(boarding_no), 0) + 1
        FROM boarding_passes
        WHERE flight_id = :flight_id
        ), :seat_no)