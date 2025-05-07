package org.example;


import org.example.entity.Weather;
import org.example.repository.CityRepository;
import org.example.repository.WeatherRepository;
import org.example.service.CityService;
import org.example.service.Impl.CityServiceImpl;
import org.example.service.Impl.WeatherServiceImpl;
import org.example.service.WeatherService;
import org.example.utils.DateUtils;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;


public class Main {

    public static void main(String[] args)  {

        String jdbcUrl = "jdbc:postgresql://localhost:5432/postgres";
        String username = "postgres";
        String password = "postgres";


        WeatherRepository weatherRepository = new WeatherRepository(jdbcUrl, username, password);
        CityRepository cityRepository = new CityRepository(jdbcUrl, username, password);
        WeatherService weatherService = new WeatherServiceImpl(weatherRepository);
        CityService cityService = new CityServiceImpl(cityRepository);

        Scanner scanner = new Scanner(System.in);

        String input;


        System.out.println("Привет, пользователь!");

        while (true) {
            System.out.println("Меню действий:");
            System.out.println("1 - Узнать прогноз погоды для определенного города\n" +
                    "2 - Выйти");
            do {
                System.out.print("Поле ввода: ");
                input = scanner.nextLine();
            } while (!input.matches("[1-2]"));

            if (input.equals("1")) {
                do {
                    System.out.println("Введите название города");
                    System.out.print("Поле ввода: ");
                    input = scanner.nextLine();
                } while (!cityService.isValidCity(input));

                System.out.println("1 - Узнать прогноз погоды на следующие 10 дней\n" +
                        "2 - Узнать прогноз погоды в заданную дату\n" +
                        "3 - Узнать прогноз погоды на сегодня\n" +
                        "4 - Узнать прогноз погоды на завтра");
                System.out.print("Поле ввода: ");
                String choice = scanner.nextLine();
                switch (choice) {
                    case "1" -> {
                        HashMap<LocalDate, Weather> wf = weatherService.getWeatherForecastOnTenDays(input);
                        for (LocalDate date: wf.keySet().stream().sorted().toList()) {
                            System.out.println("\n" + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                    + " (" + DateUtils.getDayOfWeek(date) + ")");
                            System.out.println(wf.get(date).toString());
                        }
                    }
                    case "2" -> {
                        System.out.println("Укажите дату, в которую вы хотели бы узнать прогноз погоды\n" +
                                "Формат ввода: ДД.ММ.ГГГГ\n");
                        String date;
                        while (true) {
                            System.out.print("Поле ввода: ");
                            date = scanner.nextLine();
                            if (date == null || !DateUtils.isValidDate(date)) {
                                continue;
                            }
                            break;
                        }
                        LocalDate d = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd.MM.yyyy"));
                        System.out.println("\n" + d.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                + " (" + DateUtils.getDayOfWeek(d) + ")");
                        System.out.println(weatherService.getWeatherForecastOnSetDay(input, d).toString());


                    }
                    case "3" -> {
                        LocalDate date = DateUtils.getCurrentDate();
                        System.out.println("\n" + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                + " (" + DateUtils.getDayOfWeek(DateUtils.getCurrentDate()) + ")");
                        System.out.println(weatherService.getWeatherForecastOnToday(input).toString());
                    }
                    default -> {
                        LocalDate date = DateUtils.getTomorrowDate();
                        System.out.println("\n" + date.format(DateTimeFormatter.ofPattern("dd.MM.yyyy"))
                                + " (" + DateUtils.getDayOfWeek(DateUtils.getTomorrowDate()) + ")");
                        System.out.println(weatherService.getWeatherForecastOnTomorrow(input).toString());
                    }
                }
            }
            else if (input.equals("2")) {
                System.out.println("Пока!");
                break;
            }
        }
    }
}