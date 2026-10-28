package ru.edu.pr04;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Order {
    private final Employee employee;
    private final List<OrderItem> items = new ArrayList<>();
    private OrderStatus status = OrderStatus.DRAFT;

    public Order(Employee employee) {
        this.employee = Objects.requireNonNull(employee, "Ошибка: сотрудник не может быть null.");
    }

    public void addProduct(Product product, int quantity) {
        ensureDraft();
        Objects.requireNonNull(product, "Ошибка: товар не может быть null.");
        for (OrderItem item : items) {
            if (item.getProduct().getId().equals(product.getId())) {
                item.increase(quantity);
                return;
            }
        }
        items.add(new OrderItem(product, quantity));
    }

    public void removeProduct(String productId) {
        ensureDraft();
        if (productId == null || productId.isBlank()) {
            throw new IllegalArgumentException("Ошибка: идентификатор товара для удаления пуст.");
        }
        boolean removed = items.removeIf(item -> item.getProduct().getId().equals(productId.trim()));
        if (!removed) {
            throw new IllegalArgumentException("Ошибка: товар с id " + productId + " отсутствует в заказе.");
        }
    }

    public double total() {
        double sum = 0.0;
        for (OrderItem item : items) {
            sum += item.getLineTotal();
        }
        return sum;
    }

    public void confirm() {
        ensureDraft();
        if (items.isEmpty()) {
            throw new IllegalStateException("Ошибка: нельзя подтвердить пустой заказ.");
        }
        status = OrderStatus.CONFIRMED;
    }

    private void ensureDraft() {
        if (status != OrderStatus.DRAFT) {
            throw new IllegalStateException("Ошибка: подтверждённый заказ нельзя изменять.");
        }
    }

    public Employee getEmployee() {
        return employee;
    }

    public List<OrderItem> getItems() {
        return List.copyOf(items);
    }

    public OrderStatus getStatus() {
        return status;
    }
}
