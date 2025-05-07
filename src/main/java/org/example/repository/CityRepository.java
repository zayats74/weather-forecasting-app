package org.example.repository;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CityRepository {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    public CityRepository(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

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
