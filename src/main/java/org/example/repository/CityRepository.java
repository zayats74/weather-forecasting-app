package org.example.repository;

import org.example.entity.City;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public class CityRepository {

    private final JdbcTemplate jdbcTemplate;

    private final RowMapper<City> cityRowMapper = (rs, rowNum) -> new City(
            rs.getString("city")
    );

    public static final String READ_CITY_QUERY = "SELECT city FROM cities";

    public CityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<City> getAllCities(){
        return jdbcTemplate.query(READ_CITY_QUERY, cityRowMapper);
    }
}
