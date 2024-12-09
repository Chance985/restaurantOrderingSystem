package com.Menu;

import com.Order.Order;
import com.Order.OrderException;
import com.Order.OrderService;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException;

/**
 * @author Chance Mo 1306153
 * @version 1.0
 */
public class RestaurantSystem {
    public static void main(String[] args) throws OrderException {
        MenuService menuService = new MenuService(); // 初始化菜单服务
        OrderService orderService = new OrderService(menuService); // 初始化订单服务
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                // 显示主菜单
                System.out.println("\n=== Welcome to the Restaurant System ===");
                System.out.println("1. View Menu Sorted by Price");
                System.out.println("2. View Menu Sorted by Name");
                System.out.println("3. Place an Order");
                System.out.println("4. View Recent Orders");
                System.out.println("5. View Order Queue");
                System.out.println("6. Process Next Order");
                System.out.println("7. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符

                switch (choice) {
                    case 1: // 按价格排序菜单
                        List<MenuItem> sortedByPrice = menuService.sortMenuByPrice();
                        System.out.println("\n=== Menu Sorted by Price ===");
                        menuService.displayMenu(sortedByPrice);
                        break;

                    case 2: // 按名称排序菜单
                        List<MenuItem> sortedByName = menuService.sortMenuByName();
                        System.out.println("\n=== Menu Sorted by Name ===");
                        menuService.displayMenu(sortedByName);
                        break;

                    case 3: // 点单流程
                        System.out.println("\n=== Place Your Order ===");
                        Order order = orderService.createOrder(scanner);
                        if (order != null && order.getTotalPrice() > 0) {
                            System.out.println("\nYour Order:");
                            System.out.println(order);
                            orderService.processPayment(order, scanner);
                        } else {
                            System.out.println("Order cancelled or no items were ordered.");
                        }
                        break;

                    case 4: // 查看最近订单
                        orderService.viewRecentOrders();
                        break;

                    case 5: // 查看订单队列
                        orderService.viewOrderQueue();
                        break;

                    case 6: // 处理下一个订单
                        orderService.processNextOrder();
                        break;

                    case 7: // 退出程序
                        System.out.println("Thank you for using the Restaurant System. Goodbye!");
                        scanner.close();
                        System.exit(0);
                        break;

                    default: // 非法输入
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // 清除无效输入
            } catch (Exception e) {
                System.out.println("An error occurred: " + e.getMessage());
                scanner.nextLine(); // 清除无效输入
            }
        }
    }
}