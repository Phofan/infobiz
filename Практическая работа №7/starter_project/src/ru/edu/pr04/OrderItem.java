package ru.edu.pr04;

import java.util.Objects;

public class OrderItem {
    public static final int MIN_QUANTITY = 1;
    public static final int MAX_QUANTITY = 100;
    public static final int MAX_MERGE_DELTA = 20;

    private final Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = Objects.requireNonNull(product, "Ошибка: товар не может быть null.");
        setQuantity(quantity);
    }

    private void setQuantity(int quantity) {
        if (quantity < MIN_QUANTITY || quantity > MAX_QUANTITY) {
            throw new IllegalArgumentException("Ошибка: количество позиции должно быть в диапазоне от 1 до 100.");
        }
        this.quantity = quantity;
    }

    public void increase(int delta) {
        if (delta < MIN_QUANTITY || delta > MAX_MERGE_DELTA) {
            throw new IllegalArgumentException("Ошибка: при объединении повторного товара добавляемое количество должно быть от 1 до 20.");
        }
        setQuantity(this.quantity + delta);
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getLineTotal() {
        return product.getPrice() * quantity;
    }
}
