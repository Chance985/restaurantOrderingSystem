package com.Menu;

import com.Order.Order;
import com.Order.OrderException;
import com.Order.OrderService;

import java.util.List;
import java.util.Scanner;

/**
 * @author Chance Mo 1306153
 * @version 1.0
 */
import java.util.List;
import java.util.Scanner;

public class RestaurantSystem {
    public static void main(String[] args) throws OrderException {
        MenuService menuService = new MenuService(); // 初始化菜单服务
        OrderService orderService = new OrderService(menuService);// 初始化订单服务
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n=== Welcome to the Restaurant System ===");
            System.out.println("1. View Menu Sorted by Price");
            System.out.println("2. View Menu Sorted by Name");
            System.out.println("3. Place an Order");
            System.out.println("4. Exit");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            switch (choice) {
                case 1: // 按价格排序
                    List<MenuItem> sortedByPrice = menuService.sortMenuByPrice();
                    System.out.println("\n=== Menu Sorted by Price ===");
                    menuService.displayMenu(sortedByPrice);
                    break;

                case 2: // 按名称排序
                    List<MenuItem> sortedByName = menuService.sortMenuByName();
                    System.out.println("\n=== Menu Sorted by Name ===");
                    menuService.displayMenu(sortedByName);
                    break;

                case 3: // 点单流程
                    System.out.println("\n=== Place Your Order ===");
                    Order order = orderService.createOrder(scanner);;
                    if (order.getTotalPrice() > 0) {
                        System.out.println("\nYour Order:");
                        System.out.println(order);
                        orderService.processPayment(order, scanner);;
                    } else {
                        System.out.println("No items were ordered.");
                    }
                    break;

                case 4: // 退出程序
                    System.out.println("Thank you for using the Restaurant System. Goodbye!");
                    break;

                default: // 非法输入
                    System.out.println("Invalid choice. Please try again.");
            }
        }

    }
}