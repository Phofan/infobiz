package ru.edu.pr01;

public class SelfCheck {
    public static void main(String[] args) {
        boolean ok = false;
        try {
            boolean baseAndTotalOk = OrderCalculator.calculateBase(2, 100.0) == 200.0
                    && Math.abs(OrderCalculator.calculateTotal(2, 100.0, 10.0, 20.0) - 216.0) < 0.001;
            boolean lowerBoundOk = OrderCalculator.isValid(1, 0.01, 0.0)
                    && !OrderCalculator.isValid(0, 100.0, 0.0);
            boolean upperBoundOk = OrderCalculator.isValid(10_000, 5_000_000.0, 30.0)
                    && !OrderCalculator.isValid(10_001, 100.0, 10.0)
                    && !OrderCalculator.isValid(10, 5_000_000.01, 10.0)
                    && !OrderCalculator.isValid(10, 100.0, 30.01);
            boolean negativeInvalidOk = !OrderCalculator.isValid(5, -20.0, 5.0)
                    && !OrderCalculator.isValid(5, 100.0, -1.0);
            boolean variant3Ok = !OrderCalculator.isFreeProcessing(19)
                    && OrderCalculator.isFreeProcessing(20);
            ok = baseAndTotalOk && lowerBoundOk && upperBoundOk && negativeInvalidOk && variant3Ok;
        } catch (Exception e) {
            System.out.println("FAIL exception " + e.getMessage());
        }
        System.out.println(ok ? "PASS" : "FAIL  complete TODO methods");
    }
}
