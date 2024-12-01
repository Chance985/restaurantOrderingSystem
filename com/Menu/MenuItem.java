package com.Menu;

/**
 * @author Chance Mo 1306153
 * @version 1.0
 */
// 父类 MenuItem
public class MenuItem {
    private String name;
    private double price;
    private int stock;

    public MenuItem(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    // Getters and Setters
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getStock() {
        return stock;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return "MenuItem{name='" + name + "', price=" + price + ", stock=" + stock + "}";
    }
}

