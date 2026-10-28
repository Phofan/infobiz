package ru.edu.pr03;

public class SelfCheck {
    public static void main(String[] args) {
        boolean ok = false;
        try {
            ApprovalRouter router = new ApprovalRouter();
            boolean baseOk = router.route(10_000, true, RiskLevel.LOW, true) == ApprovalRoute.SECURITY_REVIEW
                    && router.route(500_000, false, RiskLevel.LOW, false) == ApprovalRoute.FINANCE_REVIEW;
            boolean boundariesOk = router.route(499_999, true, RiskLevel.LOW, false) == ApprovalRoute.FAST_TRACK
                    && router.route(499_999, false, RiskLevel.LOW, false) == ApprovalRoute.STANDARD
                    && router.route(500_000, true, RiskLevel.LOW, false) == ApprovalRoute.FINANCE_REVIEW
                    && router.route(500_001, true, RiskLevel.LOW, false) == ApprovalRoute.FINANCE_REVIEW;
            boolean priorityOk = router.route(600_000, true, RiskLevel.HIGH, true) == ApprovalRoute.SECURITY_REVIEW
                    && router.route(600_000, true, RiskLevel.HIGH, false) == ApprovalRoute.DIRECTOR_REVIEW;
            boolean variant3Ok = router.route(100_000, true, RiskLevel.MEDIUM, false) == ApprovalRoute.FAST_TRACK
                    && router.route(100_001, true, RiskLevel.MEDIUM, false) == ApprovalRoute.STANDARD;
            boolean invalidAmountOk = fails(() -> router.route(0, false, RiskLevel.LOW, false))
                    && fails(() -> router.route(-500, false, RiskLevel.LOW, false))
                    && fails(() -> router.route(10_000, false, null, false));
            ok = baseOk && boundariesOk && priorityOk && variant3Ok && invalidAmountOk;
        } catch (Exception e) {
            System.out.println("FAIL exception " + e.getMessage());
        }
        System.out.println(ok ? "PASS" : "FAIL  complete TODO methods");
    }

    private static boolean fails(Runnable action) {
        try {
            action.run();
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }
}
