package com.Menu;

import java.util.*;
import com.Inventory.InventoryService;

public class MenuService {
    private final Map<String, MenuItem> menuMap;
    private InventoryService inventoryService;

    public MenuService() {
        menuMap = new HashMap<>();
        initializeMenu();
    }

    // 添加 setter 方法用于设置 InventoryService
    public void setInventoryService(InventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    public MenuItem getMenuItem(String name) {
        return menuMap.get(name);
    }

    private void initializeMenu() {
        addMenuItem(new MenuItem("Burger", 10.0, 0));
        addMenuItem(new MenuItem("Pizza", 12.5, 0));
        addMenuItem(new MenuItem("Pasta", 8.0, 0));
        addMenuItem(new MenuItem("Fried Rice", 7.0, 0));
        addMenuItem(new MenuItem("Noodles", 6.5, 0));
        addMenuItem(new MenuItem("Chicken Wings", 9.0, 0));
        addMenuItem(new MenuItem("Beef Steak", 15.0, 0));
        addMenuItem(new MenuItem("Grilled Fish", 14.5, 0));
        addMenuItem(new MenuItem("Sushi", 13.0, 0));
        addMenuItem(new MenuItem("Spring Rolls", 5.5, 0));
        addMenuItem(new MenuItem("Hot Dog", 6.0, 0));
        addMenuItem(new MenuItem("Sandwich", 7.5, 0));
        addMenuItem(new MenuItem("Salad", 5.0, 0));
        addMenuItem(new MenuItem("Pancake", 4.5, 0));
        addMenuItem(new MenuItem("Waffle", 6.0, 0));
        addMenuItem(new MenuItem("Ice Cream", 3.5, 0));
        addMenuItem(new MenuItem("Smoothie", 4.0, 0));
        addMenuItem(new MenuItem("Milkshake", 4.5, 0));
        addMenuItem(new MenuItem("Coffee", 3.0, 0));
        addMenuItem(new MenuItem("Tea", 2.5, 0));
        addMenuItem(new MenuItem("Soft Drink", 2.0, 0));
        addMenuItem(new MenuItem("Chocolate Cake", 6.5, 0));
        addMenuItem(new MenuItem("Cheesecake", 7.0, 0));
        addMenuItem(new MenuItem("Tiramisu", 8.0, 0));
        addMenuItem(new MenuItem("Brownie", 5.0, 0));
        addMenuItem(new MenuItem("Cookies", 4.0, 0));
        addMenuItem(new MenuItem("Donut", 3.5, 0));
        addMenuItem(new MenuItem("Fruit Platter", 8.5, 0));
        addMenuItem(new MenuItem("Vegetable Soup", 6.5, 0));
    }

    private void addMenuItem(MenuItem item) {
        menuMap.put(item.getName(), item);
    }

    public void displayMenu(List<MenuItem> menuList) {
        System.out.println("\n" + String.format("%-20s %-10s %-10s", "Name", "Price", "Available"));
        System.out.println("------------------------------------------");
        for (MenuItem item : menuList) {
            // 从库存服务获取最新的库存数量
            if (inventoryService != null) {
                int currentStock = inventoryService.getCurrentStock(item.getName());
                item.updateStock(currentStock);
            }
            System.out.println(item.toString());
        }
        System.out.println();
    }

    public List<MenuItem> sortMenuByPrice() {
        List<MenuItem> menuList = new ArrayList<>(menuMap.values());
        // 在返回前更新所有项目的库存
        if (inventoryService != null) {
            for (MenuItem item : menuList) {
                int currentStock = inventoryService.getCurrentStock(item.getName());
                item.updateStock(currentStock);
            }
        }
        menuList.sort(Comparator.comparingDouble(MenuItem::getPrice));
        return menuList;
    }

    public List<MenuItem> sortMenuByName() {
        List<MenuItem> menuList = new ArrayList<>(menuMap.values());
        // 在返回前更新所有项目的库存
        if (inventoryService != null) {
            for (MenuItem item : menuList) {
                int currentStock = inventoryService.getCurrentStock(item.getName());
                item.updateStock(currentStock);
            }
        }
        menuList.sort(Comparator.comparing(MenuItem::getName));
        return menuList;
    }

    public synchronized void syncMenuStock(String itemName, int newStock) {
        MenuItem item = menuMap.get(itemName);
        if (item != null) {
            item.updateStock(newStock);
            System.out.println("Menu stock synchronized for " + itemName + ": " + newStock);
        }
    }

    public Collection<MenuItem> getAllMenuItems() {
        Collection<MenuItem> items = menuMap.values();
        // 在返回前更新所有项目的库存
        if (inventoryService != null) {
            for (MenuItem item : items) {
                int currentStock = inventoryService.getCurrentStock(item.getName());
                item.updateStock(currentStock);
            }
        }
        return items;
    }
}