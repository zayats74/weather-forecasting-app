package org.example.service;

import java.time.LocalDate;

public interface DateService {
    LocalDate getCurrentDate();
    LocalDate getTomorrowDate();
    String getDayOfWeek(LocalDate date);
    boolean isValidYear(int year);
    boolean isValidMonth(int month);
    boolean isValidDay(int year, int month, int day);
    boolean isValidDate(String date);
}
