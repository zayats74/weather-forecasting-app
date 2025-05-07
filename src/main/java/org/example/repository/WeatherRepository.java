package org.example.repository;

import org.example.entity.Precipitation;
import org.example.entity.Weather;
import org.example.entity.Wind;

import java.sql.*;
import java.time.Instant;
import java.time.LocalDate;
import java.util.logging.Level;
import java.util.logging.Logger;


public class WeatherRepository {

    private final String jdbcUrl;
    private final String username;
    private final String password;

    //Queries
    private static final String READ_WIND_DESC_QUERY = "select id from wind_description where name = ?";
    private static final String READ_WEATHER_DESC_QUERY = "select id from weather_description where name = ?";
    public static final  String READ_PRECIPITATION_DESC_QUERY = "select id from precipitation_description where name = ?";
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


    public WeatherRepository(String jdbcUrl, String username, String password) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
    }

    public void addWeatherForecast(String city, LocalDate date, Weather weather){
       try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)){

           connection.setAutoCommit(false);

           try {
               int cityId;
               PreparedStatement checkCity = connection.prepareStatement(READ_CITY_QUERY);
               checkCity.setString(1, city);
               ResultSet rs = checkCity.executeQuery();

               if(rs.next()){
                   cityId = rs.getInt("id");
               }
               else {
                   throw new SQLException("City not found");
               }

               PreparedStatement prepStat = connection.prepareStatement(READ_SCHEDULE_QUERY);
               prepStat.setDate(1, Date.valueOf(date));
               prepStat.setString(2, city);
               ResultSet result = prepStat.executeQuery();
               if (result.next()) {
                   int scheduleId = result.getInt("id");
                   PreparedStatement updateCounter = connection.prepareStatement(UPDATE_COUNTER_SCHEDULE_QUERY);
                   updateCounter.setInt(1, scheduleId);
                   updateCounter.executeUpdate();

                   connection.commit();
               }
               else{
                   //schedule
                   PreparedStatement insertSchedule = connection.prepareStatement(INSERT_SCHEDULE_QUERY, Statement.RETURN_GENERATED_KEYS);
                   insertSchedule.setInt(1, cityId);
                   insertSchedule.setDate(2, Date.valueOf(date));
                   insertSchedule.setTimestamp(3, Timestamp.from(Instant.now()));
                   insertSchedule.executeUpdate();
                   ResultSet rsSchedule = insertSchedule.getGeneratedKeys();
                   rsSchedule.next();
                   int scheduleId = rsSchedule.getInt("id");

                   //wind
                   PreparedStatement insertWind = connection.prepareStatement(INSERT_WIND_QUERY);
                   insertWind.setInt(1, scheduleId);
                   insertWind.setDouble(2, weather.getWind().getSpeed());
                   insertWind.setDouble(3, weather.getWind().getGusts());

                   PreparedStatement windDescSt = connection.prepareStatement(READ_WIND_DESC_QUERY);
                   windDescSt.setString(1, weather.getWind().getDirection());
                   ResultSet id_wind_desc = windDescSt.executeQuery();
                   id_wind_desc.next();

                   insertWind.setInt(4, id_wind_desc.getInt("id"));
                   insertWind.executeUpdate();

                   //precipitation
                   PreparedStatement insertPrecipitation = connection.prepareStatement(INSERT_PRECIPITATION_QUERY);
                   insertPrecipitation.setInt(1, scheduleId);
                   insertPrecipitation.setDouble(2, weather.getPrecipation().getPrecipitation());

                   PreparedStatement precDescSt = connection.prepareStatement(READ_PRECIPITATION_DESC_QUERY);
                   precDescSt.setString(1, weather.getPrecipation().getDescription());
                   ResultSet id_prec_desc = precDescSt.executeQuery();
                   id_prec_desc.next();

                   insertPrecipitation.setInt(3, id_prec_desc.getInt("id"));
                   insertPrecipitation.executeUpdate();

                   //weather
                   PreparedStatement insertWeather = connection.prepareStatement(INSERT_WEATHER_QUERY);
                   insertWeather.setInt(1, scheduleId);
                   insertWeather.setDouble(2, weather.getTemperature());
                   insertWeather.setDouble(3, weather.getHumidity());
                   insertWeather.setInt(4, weather.getPressure());
                   insertWeather.setInt(5, weather.getUvIndex());
                   insertWeather.setInt(6, weather.getVisibility());

                   PreparedStatement weatherDescSt = connection.prepareStatement(READ_WEATHER_DESC_QUERY);
                   weatherDescSt.setString(1, weather.getDescription());
                   ResultSet id_weather_desc = weatherDescSt.executeQuery();
                   id_weather_desc.next();

                   insertWeather.setInt(7, id_weather_desc.getInt("id"));
                   insertWeather.executeUpdate();

                   connection.commit();
               }
           }
           catch (SQLException e) {
               connection.rollback();
               Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, e);
               throw new RuntimeException("Failed to add weather forecast to database");
           }
       }
       catch (SQLException e){
           Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, e);
           throw new RuntimeException("Failed connection to database");
       }
    }

    public Weather getWeatherForecast(String city, LocalDate date){
        Weather weather = null;
        try(Connection connection = DriverManager.getConnection(jdbcUrl, username, password)){
            PreparedStatement preparedStatement = connection.prepareStatement(READ_WEATHER_FORECASTING_QUERY);
            preparedStatement.setString(1, city);
            preparedStatement.setDate(2, Date.valueOf(date));

            try {
                ResultSet rs = preparedStatement.executeQuery();
                if(rs.next()){
                    Wind wind = new Wind(
                            rs.getString("wind_direction"),
                            rs.getDouble("wind_speed"),
                            rs.getDouble("wind_gusts")
                    );

                    Precipitation precipitation = new Precipitation(
                            rs.getString("precipitation_description"),
                            rs.getInt("precipitation")
                    );

                    weather = new Weather(
                            rs.getDouble("temperature"),
                            rs.getDouble("humidity"),
                            rs.getInt("pressure"),
                            rs.getInt("uv_index"),
                            rs.getInt("visability"),
                            rs.getString("weather_description"),
                            wind,
                            precipitation
                    );
                }
            }
            catch (SQLException e) {
                Logger.getLogger(WeatherRepository.class.getName()).log(Level.SEVERE, null, e);
                throw new RuntimeException("Failed to get data about weather from database");
            }
        }
        catch(SQLException e){
            Logger.getLogger(Connection.class.getName()).log(Level.SEVERE, null, e);
            throw new RuntimeException("Failed connection to database");
        }
        return weather;
    }


}
