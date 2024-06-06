INSERT INTO pricing_rules (flight_id, fare_conditions, min_price, max_price, avg_price)
VALUES (?, ?, ?, ?, ?)
ON CONFLICT (flight_id, fare_conditions)
    DO UPDATE SET
                  min_price = EXCLUDED.min_price,
                  max_price = EXCLUDED.max_price,
                  avg_price = EXCLUDED.avg_price;