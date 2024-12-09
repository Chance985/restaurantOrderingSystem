package com.Order;

import com.Menu.MenuItem;
import com.Menu.MenuService;
import com.Inventory.InventoryService;
import com.Inventory.InventoryException;

import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class OrderService {
    private final Stack<Order> recentOrders;
    private final Queue<Order> orderQueue;
    private final MenuService menuService;
    private static final String ORDER_FILE = "orders.dat";

    public OrderService(MenuService menuService) {
        this.menuService = menuService;
        this.recentOrders = new Stack<>();
        this.orderQueue = new LinkedBlockingQueue<>();
        loadOrders();
    }

    public Order createOrder(Scanner scanner) throws OrderException {
        Order order = new Order();
        boolean ordering = true;

        while (ordering) {
            System.out.println("\nCurrent Order: " + order);
            System.out.println("\n1. Add item to order");
            System.out.println("2. Finish order");
            System.out.println("3. Cancel order");
            System.out.print("Choose an option: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符

                switch (choice) {
                    case 1:
                        addItemToOrder(order, scanner);
                        break;
                    case 2:
                        if (order.getItems().isEmpty()) {
                            throw new OrderException("Cannot finish an empty order");
                        }
                        ordering = false;
                        break;
                    case 3:
                        if (!order.getItems().isEmpty()) {
                            restoreInventory(order);
                        }
                        return null;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                scanner.nextLine();
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        recentOrders.push(order);
        orderQueue.offer(order);
        saveOrders();
        return order;
    }

    private void addItemToOrder(Order order, Scanner scanner) throws OrderException {
        menuService.displayMenu(menuService.sortMenuByName());

        System.out.print("Enter item name: ");
        String itemName = scanner.nextLine();

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine();

        if (quantity <= 0) {
            throw new OrderException("Quantity must be greater than 0");
        }

        MenuItem item = menuService.getMenuItem(itemName);
        if (item == null) {
            throw new OrderException("Item not found: " + itemName);
        }

        InventoryService inventoryService = InventoryService.getInstance(menuService);
        try {
            if (!inventoryService.checkStock(itemName, quantity)) {
                System.out.println("Current available stock for " + itemName + ": "
                        + inventoryService.getCurrentStock(itemName));
                throw new OrderException("Insufficient stock for " + itemName);
            }

            if (inventoryService.updateStock(itemName, quantity, "SALE")) {
                order.addItem(item, quantity);
                System.out.println("Added " + quantity + " " + itemName + "(s) to your order");
                System.out.println("Remaining stock for " + itemName + ": "
                        + inventoryService.getCurrentStock(itemName));
            }
        } catch (InventoryException e) {
            throw new OrderException("Inventory error: " + e.getMessage());
        }
    }

    private void restoreInventory(Order order) {
        InventoryService inventoryService = InventoryService.getInstance(menuService);
        for (OrderItem orderItem : order.getItems()) {
            try {
                String itemName = orderItem.getItem().getName();
                int quantity = orderItem.getQuantity();
                inventoryService.updateStock(itemName, quantity, "RESTOCK");
                System.out.println("Restored " + quantity + " " + itemName + "(s) to inventory");
            } catch (InventoryException e) {
                System.err.println("Error restoring inventory: " + e.getMessage());
            }
        }
    }

    public void processPayment(Order order, Scanner scanner) {
        System.out.println("\nOrder Total: $" + String.format("%.2f", order.getTotalPrice()));
        System.out.print("Enter payment amount: $");

        try {
            double payment = scanner.nextDouble();
            scanner.nextLine();

            if (payment < order.getTotalPrice()) {
                System.out.println("Insufficient payment. Order cancelled.");
                order.setStatus(OrderStatus.CANCELLED);
                restoreInventory(order);
            } else {
                double change = payment - order.getTotalPrice();
                System.out.printf("Change: $%.2f%n", change);
                order.setStatus(OrderStatus.COMPLETED);
                System.out.println("Payment processed successfully!");
            }
        } catch (InputMismatchException e) {
            scanner.nextLine();
            System.out.println("Invalid payment amount. Order cancelled.");
            order.setStatus(OrderStatus.CANCELLED);
            restoreInventory(order);
        }

        saveOrders();
    }

    private void saveOrders() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ORDER_FILE))) {
            oos.writeObject(new ArrayList<>(recentOrders));
        } catch (IOException e) {
            System.err.println("Error saving orders: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadOrders() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(ORDER_FILE))) {
            ArrayList<Order> loadedOrders = (ArrayList<Order>) ois.readObject();
            for (Order order : loadedOrders) {
                recentOrders.push(order);
                if (order.getStatus() == OrderStatus.PENDING) {
                    orderQueue.offer(order);
                }
            }
        } catch (FileNotFoundException e) {
            // 文件不存在时从空订单开始
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading orders: " + e.getMessage());
        }
    }

    public void viewRecentOrders() {
        if (recentOrders.isEmpty()) {
            System.out.println("No recent orders.");
            return;
        }

        System.out.println("\n=== Recent Orders ===");
        Stack<Order> tempStack = new Stack<>();
        while (!recentOrders.isEmpty()) {
            Order order = recentOrders.pop();
            System.out.println(order);
            System.out.println("---------------");
            tempStack.push(order);
        }

        while (!tempStack.isEmpty()) {
            recentOrders.push(tempStack.pop());
        }
    }

    public void viewOrderQueue() {
        if (orderQueue.isEmpty()) {
            System.out.println("No orders in queue.");
            return;
        }

        System.out.println("\n=== Order Queue ===");
        for (Order order : orderQueue) {
            System.out.println(order);
            System.out.println("---------------");
        }
    }

    public void processNextOrder() {
        Order order = orderQueue.poll();
        if (order != null) {
            System.out.println("Processing order: " + order.getOrderId());
            order.setStatus(OrderStatus.PROCESSING);
            saveOrders();
        } else {
            System.out.println("No orders to process.");
        }
    }
}