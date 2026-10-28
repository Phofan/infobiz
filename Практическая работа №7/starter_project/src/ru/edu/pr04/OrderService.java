package ru.edu.pr04;

import java.util.Locale;
import java.util.Objects;
import java.util.Scanner;

public class OrderService {
    public Order createOrder(Employee employee) {
        return new Order(employee);
    }

    public void validateForConfirm(Order order) {
        Objects.requireNonNull(order, "Ошибка: заказ не может быть null.");
        if (order.getStatus() != OrderStatus.DRAFT) {
            throw new IllegalStateException("Ошибка: заказ уже подтверждён.");
        }
        if (order.getItems().isEmpty()) {
            throw new IllegalStateException("Ошибка: в заказе должна быть хотя бы одна позиция.");
        }
    }

    public void confirmOrder(Order order) {
        validateForConfirm(order);
        order.confirm();
    }

    public static void main(String[] args) {
        OrderService service = new OrderService();
        Employee employee = new Employee("E-01", "Иванов И.И.");
        Order order = service.createOrder(employee);
        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNextLine()) {
            String line = scanner.nextLine().trim();
            if (line.isEmpty() || line.startsWith("productId")) {
                continue;
            }
            String[] parts = line.split(";", -1);
            if (parts.length != 4) {
                System.out.println("Ошибка формата строки: " + line);
                continue;
            }
            try {
                Product product = new Product(parts[0].trim(), parts[1].trim(), Double.parseDouble(parts[2].trim()));
                int quantity = Integer.parseInt(parts[3].trim());
                order.addProduct(product, quantity);
                System.out.println("Добавлено: " + product.getId() + " (" + quantity + " шт.)");
            } catch (RuntimeException e) {
                System.out.println("Отклонено [" + line + "]: " + e.getMessage());
            }
        }
        if (!order.getItems().isEmpty()) {
            service.confirmOrder(order);
            System.out.printf(Locale.US, "Заказ подтверждён. Позиций: %d | Итого: %.2f руб.%n",
                    order.getItems().size(), order.total());
        }
    }
}
