SELECT
    tf.flight_id,
    tf.fare_conditions,
    MIN(tf.amount) AS min_price,
    MAX(tf.amount) AS max_price,
    ROUND(AVG(tf.amount), 2) AS avg_price
FROM ticket_flights tf
GROUP BY tf.flight_id, tf.fare_conditions;