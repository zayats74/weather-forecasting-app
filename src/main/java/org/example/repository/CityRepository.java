package org.example.repository;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
public class CityRepository {

    @Value("${spring.datasource.url}")
    private String jdbcUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;


    public List<String> getAllCities(){
        try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)){
            Statement statement = connection.createStatement();
            ResultSet allCities = statement.executeQuery("SELECT city FROM cities");
            List<String> cities = new ArrayList<>();
            while(allCities.next()) {
                cities.add(allCities.getString(1));
            }
            return cities;
        }
        catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
