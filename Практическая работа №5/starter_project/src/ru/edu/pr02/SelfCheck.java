package ru.edu.pr02;

import java.util.Arrays;

public class SelfCheck {
    public static void main(String[] args) {
        boolean ok = false;
        try {
            boolean basicOk = TimesheetAnalyzer.total(new int[]{8, 8, 8, 8, 8}) == 40
                    && TimesheetAnalyzer.overtimeDays(new int[]{9, 8, 7, 10, 8}) == 2;
            boolean lowerBoundOk = Arrays.equals(TimesheetAnalyzer.parseHours("0;0;0;0;0"), new int[]{0, 0, 0, 0, 0})
                    && TimesheetAnalyzer.total(new int[]{0, 0, 0, 0, 0}) == 0
                    && TimesheetAnalyzer.weeklyOvertime(new int[]{4, 4, 4, 4, 4}) == 0;
            boolean upperBoundOk = Arrays.equals(TimesheetAnalyzer.parseHours("16;16;16;16;16"), new int[]{16, 16, 16, 16, 16})
                    && TimesheetAnalyzer.total(new int[]{16, 16, 16, 16, 16}) == 80
                    && Math.abs(TimesheetAnalyzer.average(new int[]{9, 8, 7, 10, 8}) - 8.4) < 0.001;
            boolean variant3Ok = TimesheetAnalyzer.weeklyOvertime(new int[]{5, 5, 5, 5, 5}) == 5;
            boolean emptyErrorOk = failsWithPosition("", null);
            boolean over17ErrorOk = failsWithPosition("8;8;17;8;8", "позиции 3");
            boolean formatErrorOk = failsWithPosition("8;abc;8;8;8", "позиции 2");
            boolean negativeErrorOk = failsWithPosition("8;8;8;-1;8", "позиции 4");
            boolean wrongCountErrorOk = failsWithPosition("8;8;8;8", null);

            ok = basicOk && lowerBoundOk && upperBoundOk && variant3Ok
                    && emptyErrorOk && over17ErrorOk && formatErrorOk && negativeErrorOk && wrongCountErrorOk;
        } catch (Exception e) {
            System.out.println("FAIL exception " + e.getMessage());
        }
        System.out.println(ok ? "PASS" : "FAIL  complete TODO methods");
    }

    private static boolean failsWithPosition(String input, String expectedPart) {
        try {
            TimesheetAnalyzer.parseHours(input);
            return false;
        } catch (IllegalArgumentException e) {
            return expectedPart == null || e.getMessage().contains(expectedPart);
        }
    }
}
