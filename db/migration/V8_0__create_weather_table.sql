CREATE TABLE IF NOT EXISTS weather(
    id_schedule SERIAL PRIMARY KEY,
    temperature DOUBLE PRECISION,
    humidity DOUBLE PRECISION,
    pressure INT,
    uv_index INT,
    visability INT,
    id_weather_desc SERIAL,
    CONSTRAINT fk_id_weather_desc FOREIGN KEY (id_weather_desc) REFERENCES weather_description(id)
);