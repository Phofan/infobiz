package ru.edu.pr05;

import java.time.LocalDate;

public class SelfCheck {
    public static void main(String[] args) {
        boolean ok = false;
        try {
            BusinessTripService service = new BusinessTripService();
            LocalDate start = LocalDate.of(2026, 10, 10);
            LocalDate end = LocalDate.of(2026, 10, 12);

            BusinessTrip t1 = service.create("T-01", start, end);
            boolean initialOk = t1.getStatus() == TripStatus.DRAFT;
            t1.submit();
            boolean submitOk = t1.getStatus() == TripStatus.SUBMITTED
                    && fails(t1::submit);
            t1.approve();
            boolean approveOk = t1.getStatus() == TripStatus.APPROVED
                    && fails(() -> t1.complete(end));
            t1.complete(end.plusDays(1));
            boolean completeOk = t1.getStatus() == TripStatus.COMPLETED
                    && fails(t1::cancel);

            BusinessTrip t2 = service.create("T-02", start, end);
            t2.submit();
            boolean rejectReasonBoundaryOk = fails(() -> t2.reject("Коротко"))
                    && fails(() -> t2.reject("123456789"));
            t2.reject("Финансирование не предусмотрено");
            boolean rejectOk = t2.getStatus() == TripStatus.REJECTED
                    && "Финансирование не предусмотрено".equals(t2.getRejectionReason())
                    && fails(t2::cancel);

            BusinessTrip t3 = service.create("T-03", start, start.plusDays(20));
            boolean variant3DateOk = t3.getStatus() == TripStatus.DRAFT
                    && fails(() -> service.create("T-04", start, start.plusDays(21)))
                    && fails(() -> service.create("T-05", end, start))
                    && fails(() -> service.create("", start, end));

            boolean serviceFilterOk = service.findByStatus(TripStatus.COMPLETED).size() == 1
                    && service.findByStatus(TripStatus.REJECTED).size() == 1;

            ok = initialOk && submitOk && approveOk && completeOk
                    && rejectReasonBoundaryOk && rejectOk && variant3DateOk && serviceFilterOk;
        } catch (Exception e) {
            System.out.println("FAIL exception " + e.getMessage());
        }
        System.out.println(ok ? "PASS" : "FAIL  complete TODO methods");
    }

    private static boolean fails(Runnable action) {
        try {
            action.run();
            return false;
        } catch (RuntimeException e) {
            return true;
        }
    }
}
