package com.Menu;

import java.util.*;

/**
 * @author Chance Mo 1306153
 * @version 1.0
 */
class MenuService {
    private Map<String, MenuItem> menuMap;

    public MenuService() {
        menuMap = new HashMap<>();
    }

    public void addMenuItem(MenuItem item) {
        menuMap.put(item.getName(), item);
    }

    public MenuItem getMenuItem(String name) {
        return menuMap.get(name);
    }

    public void displayMenu() {
        System.out.println("=== Menu ===");
        for (MenuItem item : menuMap.values()) {
            System.out.println(item);
        }
    }

    public void sortMenuByPrice() {
        List<MenuItem> menuList = new ArrayList<>(menuMap.values());
        menuList.sort(Comparator.comparingDouble(MenuItem::getPrice));
        System.out.println("=== Sorted by Price ===");
        menuList.forEach(System.out::println);
    }

    public void sortMenuByName() {
        List<MenuItem> menuList = new ArrayList<>(menuMap.values());
        menuList.sort(Comparator.comparing(MenuItem::getName));
        System.out.println("=== Sorted by Name ===");
        menuList.forEach(System.out::println);
    }
}