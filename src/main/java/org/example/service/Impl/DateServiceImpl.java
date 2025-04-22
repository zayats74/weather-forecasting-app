package org.example.service.Impl;

import org.example.exception.dateException.*;
import org.example.service.DateService;

import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.Locale;

public class DateServiceImpl implements DateService {

    @Override
    public LocalDate getCurrentDate() {
        return LocalDate.now();
    }

    @Override
    public LocalDate getTomorrowDate() {
        return LocalDate.now().plusDays(1);
    }

    @Override
    public String getDayOfWeek(LocalDate date) {
        return date.getDayOfWeek().getDisplayName(TextStyle.FULL,
                Locale.forLanguageTag("ru"));
    }

    public boolean isLeapYear(int year) {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0;
    }

    @Override
    public boolean isValidYear(int year) {
        return year >= 1999 && year <= 2050;
    }

    @Override
    public boolean isValidMonth(int month) {
        return month >= 1 && month <= 12;
    }

    @Override
    public boolean isValidDay(int year, int month, int day) {
        switch (month){
            case 1, 3, 7, 5, 8, 10, 12:
                return day >= 1 && day <= 31;
            case 2:
                if (isLeapYear(year)){
                    return day >= 1 && day <= 29;
                }
                return day >= 1 && day <= 28;
            case 4, 6, 9, 11:
                return day >= 1 && day <= 30;
        }
        return false;
    }

    @Override
    public boolean isValidDate(String date) {
        try{
            int year, month;
            if(date.matches(".*\\p{L}.*")){
                throw new NonNumericDateException("В дате не могут содержаться любые буквы");
            } else if (!date.matches("\\d+\\.\\d+\\.\\d+")) {
                throw new InvalidDateFormatException("Неверный формат ввода даты\n" +
                        "Формат: ДД.ММ.ГГГГ");
            } else if (!isValidYear(year = Integer.parseInt(date.substring(6, 10)))) {
                throw new InvalidYearException("Недопустимый год.\n" +
                        "Поддерживаются только годы с 1999 по 2050");
            }
            else if (!isValidMonth(month = Integer.parseInt(date.substring(3, 5)))) {
                throw new InvalidMonthException("Некорректный номер месяца.\n" +
                        "Допустимые значения: от 1 до 12");
            } else if (!isValidDay(year, month, Integer.parseInt(date.substring(0, 2)))) {
                throw new InvalidDayException("Некорректный день заданного месяца " + year +" года\n" +
                        "Введите другой день");
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            return false;
        }
        return true;
    }
}
