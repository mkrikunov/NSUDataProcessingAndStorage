CREATE TABLE IF NOT EXISTS pricing_rules (
    flight_id VARCHAR(255),
    fare_conditions VARCHAR(255),
    min_price DOUBLE PRECISION,
    max_price DOUBLE PRECISION,
    avg_price DOUBLE PRECISION,
    PRIMARY KEY (flight_id, fare_conditions)
);