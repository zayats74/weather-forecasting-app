CREATE TABLE IF NOT EXISTS schedule(
    id SERIAL PRIMARY KEY,
    date DATE,
    city_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    count INT DEFAULT 1,
    CONSTRAINT fk_id_city FOREIGN KEY (city_id) REFERENCES cities(id)
);