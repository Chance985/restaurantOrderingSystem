package com.Menu;

import java.util.*;

/**
 * @author Chance Mo 1306153
 * @version 1.0
 */
public class MenuService {
    private Map<String, MenuItem> menuMap;

    public MenuService() {
        menuMap = new HashMap<>();
        initializeMenu(); // 初始化固定菜单
    }

    // 初始化菜单
    private void initializeMenu() {
        addMenuItem(new MenuItem("Burger", 10.0, 50));
        addMenuItem(new MenuItem("Pizza", 12.5, 30));
        addMenuItem(new MenuItem("Pasta", 8.0, 20));
        addMenuItem(new MenuItem("Fried Rice", 7.0, 25));
        addMenuItem(new MenuItem("Noodles", 6.5, 40));
        addMenuItem(new MenuItem("Chicken Wings", 9.0, 35));
        addMenuItem(new MenuItem("Beef Steak", 15.0, 15));
        addMenuItem(new MenuItem("Grilled Fish", 14.5, 10));
        addMenuItem(new MenuItem("Sushi", 13.0, 20));
        addMenuItem(new MenuItem("Spring Rolls", 5.5, 50));
        addMenuItem(new MenuItem("Hot Dog", 6.0, 60));
        addMenuItem(new MenuItem("Sandwich", 7.5, 45));
        addMenuItem(new MenuItem("Salad", 5.0, 50));
        addMenuItem(new MenuItem("Pancake", 4.5, 30));
        addMenuItem(new MenuItem("Waffle", 6.0, 25));
        addMenuItem(new MenuItem("Ice Cream", 3.5, 40));
        addMenuItem(new MenuItem("Smoothie", 4.0, 35));
        addMenuItem(new MenuItem("Milkshake", 4.5, 30));
        addMenuItem(new MenuItem("Coffee", 3.0, 50));
        addMenuItem(new MenuItem("Tea", 2.5, 60));
        addMenuItem(new MenuItem("Soft Drink", 2.0, 70));
        addMenuItem(new MenuItem("Chocolate Cake", 6.5, 20));
        addMenuItem(new MenuItem("Cheesecake", 7.0, 15));
        addMenuItem(new MenuItem("Tiramisu", 8.0, 10));
        addMenuItem(new MenuItem("Brownie", 5.0, 25));
        addMenuItem(new MenuItem("Cookies", 4.0, 50));
        addMenuItem(new MenuItem("Donut", 3.5, 45));
        addMenuItem(new MenuItem("Fruit Platter", 8.5, 25));
        addMenuItem(new MenuItem("Vegetable Soup", 6.5, 20));
    }

    // 添加菜单项
    public void addMenuItem(MenuItem item) {
        menuMap.put(item.getName(), item);
    }

    // 显示菜单
    public void displayMenu() {
        System.out.println("=== Menu ===");
        for (MenuItem item : menuMap.values()) {
            System.out.println(item);
        }
    }

    // 按价格排序
    public void sortMenuByPrice() {
        List<MenuItem> menuList = new ArrayList<>(menuMap.values());
        menuList.sort(Comparator.comparingDouble(MenuItem::getPrice));
        System.out.println("=== Menu Sorted by Price ===");
        menuList.forEach(System.out::println);
    }

    // 按名称排序
    public void sortMenuByName() {
        List<MenuItem> menuList = new ArrayList<>(menuMap.values());
        menuList.sort(Comparator.comparing(MenuItem::getName));
        System.out.println("=== Menu Sorted by Name ===");
        menuList.forEach(System.out::println);
    }
}
