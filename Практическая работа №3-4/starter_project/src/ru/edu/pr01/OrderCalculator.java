package ru.edu.pr01;

import java.util.Locale;
import java.util.Scanner;

public class OrderCalculator {
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 10_000;
    public static final double MAX_UNIT_PRICE = 5_000_000.0;
    public static final double MIN_DISCOUNT = 0.0;
    public static final double MAX_DISCOUNT = 30.0;
    public static final double VAT_RATE = 20.0;
    public static final int FREE_PROCESSING_MIN_QUANTITY = 20;

    public static boolean isValid(int quantity, double unitPrice, double discountPercent) {
        return validate(quantity, unitPrice, discountPercent) == null;
    }

    public static String validate(int quantity, double unitPrice, double discountPercent) {
        if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY) {
            return "Ошибка: количество должно быть в диапазоне от 1 до 10000.";
        }
        if (Double.isNaN(unitPrice) || Double.isInfinite(unitPrice) || unitPrice <= 0.0 || unitPrice > MAX_UNIT_PRICE) {
            return "Ошибка: цена единицы должна быть больше 0 и не превышать 5000000.00 руб.";
        }
        if (Double.isNaN(discountPercent) || Double.isInfinite(discountPercent) || discountPercent < MIN_DISCOUNT || discountPercent > MAX_DISCOUNT) {
            return "Ошибка: скидка должна быть в диапазоне от 0 до 30 процентов.";
        }
        return null;
    }

    public static boolean isFreeProcessing(int quantity) {
        return quantity >= FREE_PROCESSING_MIN_QUANTITY;
    }

    public static double calculateBase(int quantity, double unitPrice) {
        if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY || unitPrice <= 0.0 || unitPrice > MAX_UNIT_PRICE) {
            throw new IllegalArgumentException("Некорректные параметры для расчёта базовой суммы.");
        }
        return quantity * unitPrice;
    }

    public static double applyDiscount(double base, double discountPercent) {
        if (base < 0.0 || discountPercent < MIN_DISCOUNT || discountPercent > MAX_DISCOUNT) {
            throw new IllegalArgumentException("Некорректные параметры скидки.");
        }
        return base * (1.0 - discountPercent / 100.0);
    }

    public static double calculateVat(double discounted, double vatPercent) {
        if (discounted < 0.0 || vatPercent < 0.0) {
            throw new IllegalArgumentException("Некорректные параметры НДС.");
        }
        return discounted * (vatPercent / 100.0);
    }

    public static double calculateTotal(int quantity, double unitPrice, double discountPercent, double vatPercent) {
        String error = validate(quantity, unitPrice, discountPercent);
        if (error != null || vatPercent < 0.0) {
            throw new IllegalArgumentException(error != null ? error : "Некорректная ставка НДС.");
        }
        double base = calculateBase(quantity, unitPrice);
        double discounted = applyDiscount(base, discountPercent);
        double vat = calculateVat(discounted, vatPercent);
        return discounted + vat;
    }

    public static String formatOrderSummary(int quantity, double unitPrice, double discountPercent, double vatPercent) {
        String error = validate(quantity, unitPrice, discountPercent);
        if (error != null) {
            return error;
        }
        double base = calculateBase(quantity, unitPrice);
        double discounted = applyDiscount(base, discountPercent);
        double discountAmount = base - discounted;
        double vat = calculateVat(discounted, vatPercent);
        double total = calculateTotal(quantity, unitPrice, discountPercent, vatPercent);
        String processing = isFreeProcessing(quantity) ? "бесплатная обработка (кол-во >= 20)" : "стандартная обработка (кол-во < 20)";
        return String.format(Locale.US,
                "Базовая стоимость: %.2f руб. | Скидка: %.2f руб. | Сумма со скидкой: %.2f руб. | НДС: %.2f руб. | Итого: %.2f руб. | %s",
                base, discountAmount, discounted, vat, total, processing);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine()) {
            return;
        }
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty() || line.startsWith("quantity")) {
                continue;
            }
            String[] parts = line.split(";");
            if (parts.length != 3) {
                System.out.println("Ошибка: строка должна содержать 3 значения (quantity;unitPrice;discountPercent).");
                continue;
            }
            try {
                int quantity = Integer.parseInt(parts[0].trim());
                double unitPrice = Double.parseDouble(parts[1].trim());
                double discountPercent = Double.parseDouble(parts[2].trim());
                System.out.println(formatOrderSummary(quantity, unitPrice, discountPercent, VAT_RATE));
            } catch (NumberFormatException e) {
                System.out.println("Ошибка: неверный числовой формат входных данных.");
            }
        }
    }
}
