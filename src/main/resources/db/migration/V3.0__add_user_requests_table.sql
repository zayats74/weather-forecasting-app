CREATE TABLE IF NOT EXISTS user_requests (
    id SERIAL PRIMARY KEY,
    city_id INT NOT NULL,
    requested_at TIMESTAMP NOT NULL,
    CONSTRAINT fk_city_id FOREIGN KEY (city_id) REFERENCES cities(id)
);