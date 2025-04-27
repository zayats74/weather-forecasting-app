CREATE TABLE IF NOT EXISTS precipitations(
    id_schedule SERIAL NOT NULL PRIMARY KEY,
    precipitation INT,
    id_prec_desc SERIAL NOT NULL,
    CONSTRAINT fk_id_prec_desc FOREIGN KEY (id_prec_desc) REFERENCES precipitation_description(id),
    CONSTRAINT fk_id_schedule FOREIGN KEY (id_schedule) REFERENCES schedule(id)
);