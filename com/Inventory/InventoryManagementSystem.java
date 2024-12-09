package com.Inventory;

import com.Menu.MenuService;
import java.util.Scanner;
import java.util.InputMismatchException;


public class InventoryManagementSystem {
    public static void main(String[] args) {
        MenuService menuService = new MenuService();
        InventoryService inventoryService = InventoryService.getInstance(menuService);
        Scanner scanner = new Scanner(System.in);

        while (true) {
            try {
                // 显示库存管理系统菜单
                System.out.println("\n=== Inventory Management System ===");
                System.out.println("1. View Inventory Report");
                System.out.println("2. Restock Item");
                System.out.println("3. View Item History");
                System.out.println("4. Set Reorder Point");
                System.out.println("5. Exit");
                System.out.print("Choose an option: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // 消耗换行符

                switch (choice) {
                    case 1: // 查看库存报告
                        inventoryService.generateInventoryReport();
                        break;

                    case 2: // 补充库存
                        System.out.print("Enter item name: ");
                        String itemName = scanner.nextLine();
                        System.out.print("Enter quantity to restock: ");
                        int quantity = scanner.nextInt();
                        inventoryService.restockItem(itemName, quantity);
                        break;

                    case 3: // 查看商品历史
                        System.out.print("Enter item name: ");
                        String itemToView = scanner.nextLine();
                        inventoryService.viewItemHistory(itemToView);
                        break;

                    case 4: // 设置补货点
                        System.out.print("Enter item name: ");
                        String itemToSet = scanner.nextLine();
                        System.out.print("Enter new reorder point: ");
                        int reorderPoint = scanner.nextInt();
                        inventoryService.setReorderPoint(itemToSet, reorderPoint);
                        break;

                    case 5: // 退出系统
                        System.out.println("Exiting Inventory Management System. Goodbye!");
                        scanner.close();
                        System.exit(0);
                        break;

                    default:
                        System.out.println("Invalid choice. Please try again.");
                }
            } catch (InputMismatchException e) {
                System.out.println("Invalid input. Please enter a number.");
                scanner.nextLine(); // 清除无效输入
            } catch (InventoryException e) {
                System.out.println("Inventory error: " + e.getMessage());
            }
        }
    }
}