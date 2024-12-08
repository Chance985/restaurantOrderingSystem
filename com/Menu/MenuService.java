package com.Menu;

import java.util.*;

/**
 * @author Chance Mo 1306153
 * @version 1.0
 */
import java.util.*;

 public class MenuService {
    private Map<String, MenuItem> menuMap;

    public MenuService() {
        menuMap = new HashMap<>();
        initializeMenu(); // 初始化固定菜单
    }
     public MenuItem getMenuItem(String name) {
         return menuMap.get(name);
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

    // 显示菜单（格式化输出）
    public void displayMenu(List<MenuItem> menuList) {
        System.out.println(String.format("%-20s %-10s %-10s", "Name", "Price", "Stock"));
        System.out.println("------------------------------------------");
        for (MenuItem item : menuList) {
            System.out.println(String.format("%-20s $%-9.2f %-10d", item.getName(), item.getPrice(), item.getStock()));
        }
    }

    // 按价格排序菜单
    public List<MenuItem> sortMenuByPrice() {
        List<MenuItem> menuList = new ArrayList<>(menuMap.values());
        menuList.sort(Comparator.comparingDouble(MenuItem::getPrice));
        return menuList;
    }

    // 按名称排序菜单
    public List<MenuItem> sortMenuByName() {
        List<MenuItem> menuList = new ArrayList<>(menuMap.values());
        menuList.sort(Comparator.comparing(MenuItem::getName));
        return menuList;
    }


}


