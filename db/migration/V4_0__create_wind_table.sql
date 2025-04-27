CREATE TABLE IF NOT EXISTS wind(
    id_schedule SERIAL NOT NULL PRIMARY KEY,
    speed DOUBLE PRECISION,
    gusts DOUBLE PRECISION,
    id_wind_desc SERIAL NOT NULL,
    CONSTRAINT fk_id_wind_desc FOREIGN KEY (id_wind_desc) REFERENCES wind_description(id),
    CONSTRAINT fk_id_schedule FOREIGN KEY (id_schedule) REFERENCES schedule(id)
);