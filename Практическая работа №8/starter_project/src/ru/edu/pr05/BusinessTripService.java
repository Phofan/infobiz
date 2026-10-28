package ru.edu.pr05;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class BusinessTripService {
    private final List<BusinessTrip> trips = new ArrayList<>();

    public BusinessTrip create(String id, LocalDate start, LocalDate end) {
        BusinessTrip trip = new BusinessTrip(id, start, end);
        trips.add(trip);
        return trip;
    }

    public List<BusinessTrip> findByStatus(TripStatus status) {
        List<BusinessTrip> result = new ArrayList<>();
        for (BusinessTrip trip : trips) {
            if (trip.getStatus() == status) {
                result.add(trip);
            }
        }
        return List.copyOf(result);
    }

    public static void main(String[] args) {
        BusinessTripService service = new BusinessTripService();
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty() || line.startsWith("id;")) {
                continue;
            }
            String[] parts = line.split(";", -1);
            if (parts.length < 4) {
                System.out.println("Ошибка формата строки: " + line);
                continue;
            }
            try {
                String id = parts[0].trim();
                LocalDate start = LocalDate.parse(parts[1].trim());
                LocalDate end = LocalDate.parse(parts[2].trim());
                String action = parts[3].trim().toUpperCase();
                BusinessTrip trip = service.create(id, start, end);
                if ("SUBMIT".equals(action)) {
                    trip.submit();
                } else if ("APPROVE".equals(action)) {
                    trip.submit();
                    trip.approve();
                } else if ("CANCEL".equals(action)) {
                    trip.cancel();
                }
                System.out.println(id + " -> " + trip.getStatus());
            } catch (RuntimeException e) {
                System.out.println("Отклонено [" + line + "]: " + e.getMessage());
            }
        }
    }
}
