package ru.edu.pr04;

public class SelfCheck {
    public static void main(String[] args) {
        boolean ok = false;
        try {
            OrderService service = new OrderService();
            Employee emp = new Employee("E1", "Петров П.П.");
            Product p1 = new Product("M01", "Monitor 27", 32990.0);
            Product p2 = new Product("K01", "Keyboard", 4490.0);

            Order order = service.createOrder(emp);
            order.addProduct(p1, 2);
            order.addProduct(p2, 3);
            order.addProduct(p1, 1);

            boolean mergeAndTotalOk = order.getItems().size() == 2
                    && order.getItems().get(0).getQuantity() == 3
                    && Math.abs(order.total() - (3 * 32990.0 + 3 * 4490.0)) < 0.001;

            order.removeProduct("K01");
            boolean removeOk = order.getItems().size() == 1
                    && Math.abs(order.total() - 3 * 32990.0) < 0.001;

            boolean variant3Ok = true;
            order.addProduct(p1, 20);
            variant3Ok = variant3Ok && order.getItems().get(0).getQuantity() == 23;
            variant3Ok = variant3Ok && fails(() -> order.addProduct(p1, 21));

            boolean boundaryQtyOk = true;
            Order boundaryOrder = service.createOrder(emp);
            boundaryOrder.addProduct(p2, 1);
            Order maxOrder = service.createOrder(emp);
            maxOrder.addProduct(p2, 100);
            boundaryQtyOk = boundaryQtyOk
                    && boundaryOrder.getItems().get(0).getQuantity() == 1
                    && maxOrder.getItems().get(0).getQuantity() == 100
                    && fails(() -> service.createOrder(emp).addProduct(p2, 0))
                    && fails(() -> service.createOrder(emp).addProduct(p2, 101))
                    && fails(() -> maxOrder.addProduct(p2, 1));

            boolean invalidProductOk = fails(() -> new Product("", "Mouse", 1000.0))
                    && fails(() -> new Product("P2", "", 1000.0))
                    && fails(() -> new Product("P2", "Mouse", 0.0))
                    && fails(() -> new Product("P2", "Mouse", -100.0));

            service.confirmOrder(order);
            boolean immutableAfterConfirmOk = order.getStatus() == OrderStatus.CONFIRMED
                    && fails(() -> order.addProduct(p2, 1))
                    && fails(() -> order.removeProduct("M01"))
                    && fails(() -> order.getItems().clear());

            ok = new Product("P1", "Mouse", 1000).getPrice() == 1000
                    && mergeAndTotalOk && removeOk && variant3Ok
                    && boundaryQtyOk && invalidProductOk && immutableAfterConfirmOk;
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
