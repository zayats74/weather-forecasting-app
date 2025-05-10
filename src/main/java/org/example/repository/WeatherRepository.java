package org.example.repository;

import org.example.entity.Precipitation;
import org.example.entity.Weather;
import org.example.entity.Wind;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;

@Repository
public class WeatherRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<Wind> windRowMapper = ((rs, rowNum) -> new Wind(
            rs.getString("wind_direction"),
            rs.getDouble("wind_speed"),
            rs.getDouble("wind_gusts")
    ));

    private final RowMapper<Precipitation> precipitationRowMapper = ((rs, rowNum) -> new Precipitation(
            rs.getString("precipitation_description"),
            rs.getInt("precipitation")
    ));

    private final RowMapper<Weather> weatherRowMapper = ((rs, rowNum) -> new Weather(
            rs.getDouble("temperature"),
            rs.getDouble("humidity"),
            rs.getInt("pressure"),
            rs.getInt("uv_index"),
            rs.getInt("visability"),
            rs.getString("weather_description"),
            windRowMapper.mapRow(rs, rowNum),
            precipitationRowMapper.mapRow(rs, rowNum)

    ));

    //Queries
    private static final String READ_WIND_DESC_QUERY = "select id from wind_description where name = ?";
    private static final String READ_WEATHER_DESC_QUERY = "select id from weather_description where name = ?";
    public static final String READ_PRECIPITATION_DESC_QUERY = "select id from precipitation_description where name = ?";
    public static final String READ_SCHEDULE_QUERY = "select id from schedule where date = ? and city_id = (select id from cities where city = ?)";
    public static final String READ_CITY_QUERY = "select id from cities where city = ?";
    public static final String READ_WEATHER_FORECASTING_QUERY = """
            SELECT w.temperature, w.humidity, w.pressure, w.uv_index, w.visability,
                    wd.name as weather_description,
                    wi.speed as wind_speed, wi.gusts as wind_gusts,
                    wid.name as wind_direction,
                    p.precipitation, pd.name as precipitation_description
            FROM schedule s
            INNER JOIN cities c ON s.city_id = c.id
            INNER JOIN weather w ON w.id_schedule = s.id
            INNER JOIN weather_description wd ON wd.id = w.id_weather_desc
            INNER JOIN precipitations p ON p.id_schedule = s.id
            INNER JOIN precipitation_description pd ON pd.id = p.id_prec_desc
            INNER JOIN wind wi ON wi.id_schedule = s.id
            INNER JOIN wind_description wid ON wid.id = wi.id_wind_desc
            WHERE city = ? AND date = ?;
            """;

    public static final String INSERT_WIND_QUERY = "insert into wind (id_schedule, speed, gusts, id_wind_desc) values (?, ?, ?, ?)";
    public static final String INSERT_PRECIPITATION_QUERY = "insert into precipitations (id_schedule, precipitation, id_prec_desc) values (?, ?, ?)";
    public static final String INSERT_WEATHER_QUERY = "insert into weather (id_schedule, temperature, humidity, pressure, uv_index, visability, id_weather_desc) " +
            "values (?, ?, ?, ?, ?, ?, ?)";
    public static final String INSERT_SCHEDULE_QUERY = "insert into schedule (city_id, date, created_at) values (?, ?, ?) returning id";

    public static final String UPDATE_COUNTER_SCHEDULE_QUERY = "update schedule set count = count + 1, updated_at = NOW() where id = ?";

    public WeatherRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private Integer getDescription(String query, String description) {
        return jdbcTemplate.queryForObject(query,
                (rs, rowNum) -> rs.getInt("id"), description);
    }

    @Transactional
    public void addWeatherForecast(String city, LocalDate date, Weather weather) {
        try {
            Integer cityId = jdbcTemplate.queryForObject(READ_CITY_QUERY,
                    Integer.class,
                    city);

            if (cityId == null) {
                throw new SQLException("City not found");
            }

            Integer scheduleId;
            try {
                scheduleId = jdbcTemplate.queryForObject(READ_SCHEDULE_QUERY,
                        Integer.class,
                        Date.valueOf(date), city);
                jdbcTemplate.update(UPDATE_COUNTER_SCHEDULE_QUERY, scheduleId);
            } catch (EmptyResultDataAccessException e) {
                scheduleId = jdbcTemplate.queryForObject(INSERT_SCHEDULE_QUERY,
                        Integer.class,
                        cityId,
                        Date.valueOf(date),
                        Timestamp.from(Instant.now()));

                //wind
                Integer windDescId = getDescription(READ_WIND_DESC_QUERY, weather.getWind().getDirection());
                jdbcTemplate.update(INSERT_WIND_QUERY,
                        scheduleId,
                        weather.getWind().getSpeed(),
                        weather.getWind().getGusts(),
                        windDescId);

                //precipitation
                Integer precipitationDescId = getDescription(READ_PRECIPITATION_DESC_QUERY, weather.getPrecipation().getDescription());
                jdbcTemplate.update(INSERT_PRECIPITATION_QUERY,
                        scheduleId,
                        weather.getPrecipation().getPrecipitation(),
                        precipitationDescId);

                //weather
                Integer weatherDescId = getDescription(READ_WEATHER_DESC_QUERY, weather.getDescription());
                jdbcTemplate.update(INSERT_WEATHER_QUERY,
                        scheduleId,
                        weather.getTemperature(),
                        weather.getHumidity(),
                        weather.getPressure(),
                        weather.getUvIndex(),
                        weather.getVisibility(),
                        weatherDescId);
            }
        } catch (DataAccessException | SQLException e) {
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException("Failed to add weather forecast to database");
        }
    }

    public Weather getWeatherForecast(String city, LocalDate date) {
        try {
            return jdbcTemplate.queryForObject(READ_WEATHER_FORECASTING_QUERY,
                    weatherRowMapper, city, Date.valueOf(date));
        } catch (EmptyResultDataAccessException e) {
            Logger.getLogger(WeatherRepository.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException("Failed to get data about weather from database");
        }
    }


}
