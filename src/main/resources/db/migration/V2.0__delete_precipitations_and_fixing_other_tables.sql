DROP TABLE IF EXISTS precipitations;

DROP TABLE IF EXISTS precipitation_description;

ALTER TABLE wind_description
ADD COLUMN degree_start DOUBLE PRECISION,
ADD COLUMN degree_end DOUBLE PRECISION;

ALTER TABLE weather_description
ADD COLUMN min_cloud_cover DOUBLE PRECISION,
ADD COLUMN max_cloud_cover DOUBLE PRECISION;

ALTER TABLE schedule
ADD COLUMN time TIME;

ALTER TABLE wind
DROP COLUMN gusts;