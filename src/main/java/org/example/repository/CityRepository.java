package org.example.repository;

import org.example.entity.City;
import org.springframework.dao.EmptyResultDataAccessException;
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

    public static final String READ_CITIES_QUERY = "SELECT city FROM cities";
    public static final String READ_CITY_QUERY = "SELECT id FROM cities WHERE city = ?";

    public CityRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }


    public List<City> getAllCities(){
        return jdbcTemplate.query(READ_CITIES_QUERY, cityRowMapper);
    }

    public boolean containsCity(String city){
        try {
            return jdbcTemplate.queryForObject(READ_CITY_QUERY, Integer.class, city) != null;
        }
        catch (EmptyResultDataAccessException e){
            return false;
        }
    }

}
