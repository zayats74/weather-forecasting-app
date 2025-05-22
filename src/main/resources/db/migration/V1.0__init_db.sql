CREATE TABLE IF NOT EXISTS cities(
    id SERIAL PRIMARY KEY,
    city VARCHAR(255) NOT NULL
);

CREATE TABLE IF NOT EXISTS schedule(
    id SERIAL PRIMARY KEY,
    date DATE,
    city_id INT NOT NULL,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP,
    count INT DEFAULT 1,
    CONSTRAINT fk_id_city FOREIGN KEY (city_id) REFERENCES cities(id)
);

CREATE TABLE IF NOT EXISTS wind_description(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS wind(
    id_schedule SERIAL NOT NULL PRIMARY KEY,
    speed DOUBLE PRECISION,
    gusts DOUBLE PRECISION,
    id_wind_desc SERIAL NOT NULL,
    CONSTRAINT fk_id_wind_desc FOREIGN KEY (id_wind_desc) REFERENCES wind_description(id),
    CONSTRAINT fk_id_schedule FOREIGN KEY (id_schedule) REFERENCES schedule(id)
);

CREATE TABLE IF NOT EXISTS precipitation_description(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS precipitations(
    id_schedule SERIAL NOT NULL PRIMARY KEY,
    precipitation INT,
    id_prec_desc SERIAL NOT NULL,
    CONSTRAINT fk_id_prec_desc FOREIGN KEY (id_prec_desc) REFERENCES precipitation_description(id),
    CONSTRAINT fk_id_schedule FOREIGN KEY (id_schedule) REFERENCES schedule(id)
);

CREATE TABLE IF NOT EXISTS weather_description(
    id SERIAL PRIMARY KEY,
    name VARCHAR(255)
);

CREATE TABLE IF NOT EXISTS weather(
    id_schedule SERIAL PRIMARY KEY,
    temperature DOUBLE PRECISION,
    humidity DOUBLE PRECISION,
    pressure INT,
    uv_index INT,
    visibility INT,
    id_weather_desc INT,
    CONSTRAINT fk_id_weather_desc FOREIGN KEY (id_weather_desc) REFERENCES weather_description(id)
);