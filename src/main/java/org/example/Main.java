package org.example;


import org.example.entity.Wheather;
import org.example.exception.InvalidCityFormatException;
import org.example.exception.InvalidCityNameException;
import org.example.exception.NonCyrillicCharactersException;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;



public class Main {

    public static boolean isCyrillic(String str){
        return str.matches("[а-яА-Я]+");
    }

    public static void main(String[] args) {

        List<String> Allcities = new ArrayList<String>();

        try(BufferedReader reader = Files.newBufferedReader(Paths.get("src/main/java/org/example/file/cities.txt"))  )      {
            Allcities = reader.lines().toList();
        }
        catch(IOException ex){

            System.out.println(ex.getMessage());
        }

        System.out.println("Привет, пользователь!");
        Scanner scanner = new Scanner(System.in);

        String input;

        HashMap<String, HashMap<String, Wheather>> wheatherForecast = new HashMap<>();
        Map<Integer, String> daysofWeek = Map.of(
                1, "Понедельник",
                2, "Вторник",
                3, "Среда",
                4, "Четверг",
                5, "Пятница",
                6, "Суббота",
                7, "Воскресенье");
        Random random = new Random(55);

        while (true) {
            System.out.println("Меню действий:");
            System.out.println("1 - Узнать прогноз погоды для определенного города\n" +
                    "2 - Выйти");
            while (true) {
                System.out.print("Поле ввода: ");
                input = scanner.nextLine();
                if (input.matches("[1-2]")) {
                    break;
                }
            }
            if (input.equals("1")) {
                while(true) {
                    System.out.println("Введите название города");
                    try {
                        System.out.print("Поле ввода: ");
                        input = scanner.nextLine();
                        if (!isCyrillic(input)) {
                            throw new NonCyrillicCharactersException("Название города должно быть введено кириллицей.");
                        } else if ('А' > input.charAt(0) || input.charAt(0) > 'Я') {
                            throw new InvalidCityFormatException("Название города должно начинаться с заглавной буквы");
                        } else if (!Allcities.contains(input)) {
                            throw new InvalidCityNameException("Города с таким названием в России не существует.");
                        }
                        break;
                    } catch (NonCyrillicCharactersException | InvalidCityFormatException | InvalidCityNameException ex) {
                        System.out.println(ex.getMessage());
                    }
                }

                if (!wheatherForecast.containsKey(input)) {
                    wheatherForecast.put(input, new HashMap<>());
                    for (int i = 1; i <= 7; i++){
                        wheatherForecast.get(input).put(daysofWeek.get(i), new Wheather(random));
                    }
                }

                System.out.println("1 - Узнать прогноз погоды на неделю\n" +
                        "2 - Узнать прогноз погоды в заданный день недели");
                System.out.print("Поле ввода: ");
                String choice = scanner.nextLine();
                if(choice.equals("1")) {
                    for (int i = 1; i <= 7; i++){
                        System.out.println("\n" + daysofWeek.get(i));
                        System.out.println(wheatherForecast.get(input).get(daysofWeek.get(i)).toString());
                    }
                }
                else {
                    System.out.println("Укажите день недели, в который вы бы хотели узнать прогноз погоды:\n" +
                                           "1 - Понедельник\n" +
                                           "2 - Вторник\n" +
                                           "3 - Среда\n" +
                                           "4 - Четверг\n" +
                                           "5 - Пятница\n" +
                                           "6 - Суббота\n" +
                                           "7 - Воскресенье\n");
                    while (true) {
                        System.out.print("Поле ввода: ");
                        String dayOfWeek = scanner.nextLine();
                        if (dayOfWeek == null || !dayOfWeek.matches("[1-7]")) {
                            continue;
                        }
                        System.out.println(daysofWeek.get(Integer.parseInt(dayOfWeek)));
                        System.out.println(wheatherForecast.get(input).get(daysofWeek.get(Integer.parseInt(dayOfWeek))).toString());
                        break;
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