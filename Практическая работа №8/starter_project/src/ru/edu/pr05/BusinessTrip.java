package ru.edu.pr05;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class BusinessTrip {
    public static final int MIN_REJECTION_REASON_LENGTH = 10;
    public static final long MAX_TRIP_DAYS = 20L;

    private final String id;
    private final LocalDate start;
    private final LocalDate end;
    private TripStatus status = TripStatus.DRAFT;
    private String rejectionReason;

    public BusinessTrip(String id, LocalDate start, LocalDate end) {
        if (id == null || id.isBlank()) {
            throw new IllegalArgumentException("Ошибка: идентификатор командировки обязателен.");
        }
        if (start == null || end == null) {
            throw new IllegalArgumentException("Ошибка: даты начала и окончания обязательны.");
        }
        if (end.isBefore(start)) {
            throw new IllegalArgumentException("Ошибка: дата окончания не может быть раньше даты начала.");
        }
        long days = ChronoUnit.DAYS.between(start, end);
        if (days > MAX_TRIP_DAYS) {
            throw new IllegalArgumentException("Ошибка: длительность командировки не должна превышать 20 дней.");
        }
        this.id = id.trim();
        this.start = start;
        this.end = end;
    }

    public void submit() {
        if (status != TripStatus.DRAFT) {
            throw new IllegalStateException("Ошибка: отправка возможна только из состояния DRAFT.");
        }
        status = TripStatus.SUBMITTED;
    }

    public void approve() {
        if (status != TripStatus.SUBMITTED) {
            throw new IllegalStateException("Ошибка: согласование возможно только из состояния SUBMITTED.");
        }
        status = TripStatus.APPROVED;
    }

    public void reject(String reason) {
        if (status != TripStatus.SUBMITTED) {
            throw new IllegalStateException("Ошибка: отклонение возможно только из состояния SUBMITTED.");
        }
        if (reason == null || reason.trim().length() < MIN_REJECTION_REASON_LENGTH) {
            throw new IllegalArgumentException("Ошибка: причина отклонения обязательна и должна содержать минимум 10 символов.");
        }
        this.rejectionReason = reason.trim();
        this.status = TripStatus.REJECTED;
    }

    public void cancel() {
        if (status != TripStatus.DRAFT && status != TripStatus.SUBMITTED && status != TripStatus.APPROVED) {
            throw new IllegalStateException("Ошибка: отмена запрещена для текущего состояния " + status);
        }
        status = TripStatus.CANCELLED;
    }

    public void complete(LocalDate today) {
        if (status != TripStatus.APPROVED) {
            throw new IllegalStateException("Ошибка: завершение возможно только из состояния APPROVED.");
        }
        if (today == null) {
            throw new IllegalArgumentException("Ошибка: текущая дата не указана.");
        }
        if (!today.isAfter(end)) {
            throw new IllegalStateException("Ошибка: завершить командировку можно только после даты её окончания.");
        }
        status = TripStatus.COMPLETED;
    }

    public String getId() {
        return id;
    }

    public LocalDate getStart() {
        return start;
    }

    public LocalDate getEnd() {
        return end;
    }

    public TripStatus getStatus() {
        return status;
    }

    public String getRejectionReason() {
        return rejectionReason;
    }
}
