package ru.edu.pr03;

import java.util.Scanner;

public class ApprovalRouter {
    public static final long FINANCE_LIMIT = 500_000L;
    public static final int URGENT_MEDIUM_LIMIT_PERCENT = 20;

    public ApprovalRoute route(long amount, boolean urgent, RiskLevel risk, boolean containsPersonalData) {
        validateInput(amount, risk);
        if (requiresSecurityReview(containsPersonalData)) {
            return ApprovalRoute.SECURITY_REVIEW;
        } else if (requiresDirectorReview(risk)) {
            return ApprovalRoute.DIRECTOR_REVIEW;
        } else if (requiresFinanceReview(amount)) {
            return ApprovalRoute.FINANCE_REVIEW;
        } else if (isEligibleForFastTrack(amount, urgent, risk)) {
            return ApprovalRoute.FAST_TRACK;
        } else {
            return ApprovalRoute.STANDARD;
        }
    }

    private void validateInput(long amount, RiskLevel risk) {
        if (amount <= 0) {
            throw new IllegalArgumentException("Ошибка: сумма заявки должна быть положительной.");
        }
        if (risk == null) {
            throw new IllegalArgumentException("Ошибка: уровень риска не указан.");
        }
    }

    private boolean requiresSecurityReview(boolean containsPersonalData) {
        return containsPersonalData;
    }

    private boolean requiresDirectorReview(RiskLevel risk) {
        return risk == RiskLevel.HIGH;
    }

    private boolean requiresFinanceReview(long amount) {
        return amount >= FINANCE_LIMIT;
    }

    private boolean isEligibleForFastTrack(long amount, boolean urgent, RiskLevel risk) {
        if (!urgent || amount >= FINANCE_LIMIT) {
            return false;
        }
        long mediumLimit = FINANCE_LIMIT * URGENT_MEDIUM_LIMIT_PERCENT / 100L;
        return switch (risk) {
            case LOW -> true;
            case MEDIUM -> amount <= mediumLimit;
            case HIGH -> false;
        };
    }

    public static void main(String[] args) {
        ApprovalRouter router = new ApprovalRouter();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty() || line.startsWith("amount")) {
                continue;
            }
            String[] parts = line.split(";", -1);
            if (parts.length != 4) {
                System.out.println("Ошибка: строка должна содержать 4 параметра (amount;urgent;risk;personal).");
                continue;
            }
            try {
                long amount = Long.parseLong(parts[0].trim());
                boolean urgent = parseStrictBoolean(parts[1].trim());
                RiskLevel risk = RiskLevel.valueOf(parts[2].trim().toUpperCase());
                boolean personal = parseStrictBoolean(parts[3].trim());
                ApprovalRoute result = router.route(amount, urgent, risk, personal);
                System.out.println(line + " -> " + result);
            } catch (IllegalArgumentException e) {
                System.out.println("Ошибка для [" + line + "]: " + e.getMessage());
            }
        }
    }

    private static boolean parseStrictBoolean(String value) {
        if ("true".equalsIgnoreCase(value)) {
            return true;
        }
        if ("false".equalsIgnoreCase(value)) {
            return false;
        }
        throw new IllegalArgumentException("Некорректное логическое значение: " + value);
    }
}
