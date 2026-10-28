package ru.edu.pr02;

import java.util.Locale;
import java.util.Scanner;

public class TimesheetAnalyzer {
    public static final int DAYS_IN_WEEK = 5;
    public static final int MIN_HOURS = 0;
    public static final int MAX_HOURS = 16;
    public static final int DAILY_NORM = 8;
    public static final int WEEKLY_NORM = 20;

    public static int[] parseHours(String line) {
        if (line == null || line.trim().isEmpty()) {
            throw new IllegalArgumentException("Ошибка: строка табеля пуста.");
        }
        String[] parts = line.trim().split(";", -1);
        if (parts.length != DAYS_IN_WEEK) {
            throw new IllegalArgumentException("Ошибка: строка должна содержать ровно 5 значений, получено: " + parts.length);
        }
        int[] result = new int[DAYS_IN_WEEK];
        for (int i = 0; i < DAYS_IN_WEEK; i++) {
            String token = parts[i].trim();
            if (token.isEmpty()) {
                throw new IllegalArgumentException("Ошибка в позиции " + (i + 1) + ": пустое значение.");
            }
            int value;
            try {
                value = Integer.parseInt(token);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Ошибка в позиции " + (i + 1) + ": неверный формат числа '" + token + "'.");
            }
            if (value < MIN_HOURS || value > MAX_HOURS) {
                throw new IllegalArgumentException("Ошибка в позиции " + (i + 1) + ": значение " + value + " вне допустимого диапазона 0..16.");
            }
            result[i] = value;
        }
        return result;
    }

    public static int total(int[] hours) {
        validateArray(hours);
        int sum = 0;
        for (int i = 0; i < hours.length; i++) {
            sum += hours[i];
        }
        return sum;
    }

    public static double average(int[] hours) {
        validateArray(hours);
        return (double) total(hours) / hours.length;
    }

    public static int overtimeDays(int[] hours) {
        validateArray(hours);
        int count = 0;
        int i = 0;
        while (i < hours.length) {
            if (hours[i] > DAILY_NORM) {
                count++;
            }
            i++;
        }
        return count;
    }

    public static int weeklyOvertime(int[] hours) {
        int sum = total(hours);
        return Math.max(0, sum - WEEKLY_NORM);
    }

    private static void validateArray(int[] hours) {
        if (hours == null || hours.length != DAYS_IN_WEEK) {
            throw new IllegalArgumentException("Ошибка: массив часов должен содержать ровно 5 элементов.");
        }
        for (int i = 0; i < hours.length; i++) {
            if (hours[i] < MIN_HOURS || hours[i] > MAX_HOURS) {
                throw new IllegalArgumentException("Ошибка в позиции " + (i + 1) + ": недопустимое количество часов " + hours[i]);
            }
        }
    }

    public static String formatReport(String line) {
        int[] hours = parseHours(line);
        int sum = total(hours);
        double avg = average(hours);
        int overDays = overtimeDays(hours);
        int weekOver = weeklyOvertime(hours);
        return String.format(Locale.US,
                "Сумма: %d ч | Среднее: %.2f ч | Дней с переработкой: %d | Недельная переработка (норма %d ч): %d ч",
                sum, avg, overDays, WEEKLY_NORM, weekOver);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine();
            try {
                System.out.println(formatReport(line));
            } catch (IllegalArgumentException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
