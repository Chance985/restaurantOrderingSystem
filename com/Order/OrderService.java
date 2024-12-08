package com.Order;
import com.Menu.MenuItem;
import com.Menu.MenuService;

import java.io.*;
import java.util.*;
import java.util.concurrent.LinkedBlockingQueue;

public class OrderService {
    private final Stack<Order> recentOrders;  // 使用栈存储最近的订单
    private final Queue<Order> orderQueue;    // 使用队列处理订单
    private final MenuService menuService;
    private static final String ORDER_FILE = "orders.dat";
    private static final int LOW_STOCK_THRESHOLD = 5;

    public OrderService(MenuService menuService) {
        this.menuService = menuService;
        this.recentOrders = new Stack<>();
        this.orderQueue = new LinkedBlockingQueue<>();
        loadOrders();
    }

    // 创建新订单
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
                        return null;
                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                scanner.nextLine(); // 清除无效输入
                System.out.println("Invalid input. Please enter a number.");
            }
        }

        // 将订单添加到最近订单栈和处理队列中
        recentOrders.push(order);
        orderQueue.offer(order);
        saveOrders();
        return order;
    }

    // 向订单添加商品并检查库存
    private void addItemToOrder(Order order, Scanner scanner) throws OrderException {
        menuService.displayMenu(menuService.sortMenuByName());

        System.out.print("Enter item name: ");
        String itemName = scanner.nextLine();

        System.out.print("Enter quantity: ");
        int quantity = scanner.nextInt();
        scanner.nextLine(); // 消耗换行符

        MenuItem item = menuService.getMenuItem(itemName);
        if (item == null) {
            throw new OrderException("Item not found: " + itemName);
        }

        if (item.getStock() < quantity) {
            throw new OrderException("Insufficient stock for " + itemName);
        }

        if (item.getStock() - quantity <= LOW_STOCK_THRESHOLD) {
            System.out.println("Warning: Low stock alert for " + itemName);
        }

        item.updateStock(quantity);
        order.addItem(item, quantity);
    }

    // 处理订单支付
    public void processPayment(Order order, Scanner scanner) {
        System.out.println("\nOrder Total: $" + String.format("%.2f", order.getTotalPrice()));
        System.out.print("Enter payment amount: $");

        try {
            double payment = scanner.nextDouble();
            scanner.nextLine(); // 消耗换行符

            if (payment < order.getTotalPrice()) {
                System.out.println("Insufficient payment. Order cancelled.");
                order.setStatus(OrderStatus.CANCELLED);
            } else {
                double change = payment - order.getTotalPrice();
                System.out.printf("Change: $%.2f%n", change);
                order.setStatus(OrderStatus.COMPLETED);
                System.out.println("Payment processed successfully!");
            }
        } catch (InputMismatchException e) {
            scanner.nextLine(); // 清除无效输入
            System.out.println("Invalid payment amount. Order cancelled.");
            order.setStatus(OrderStatus.CANCELLED);
        }

        saveOrders();
    }

    // 将订单保存到文件
    private void saveOrders() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(ORDER_FILE))) {
            oos.writeObject(new ArrayList<>(recentOrders));
        } catch (IOException e) {
            System.err.println("Error saving orders: " + e.getMessage());
        }
    }

    // 从文件加载订单
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
            // 文件不存在，从空订单开始
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading orders: " + e.getMessage());
        }
    }

    // 查看最近的订单（使用栈）
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

        // 恢复原始栈
        while (!tempStack.isEmpty()) {
            recentOrders.push(tempStack.pop());
        }
    }

    // 查看订单队列
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

    // 处理队列中的下一个订单
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