package com.Menu;

import java.io.Serializable;

public class MenuItem implements Serializable {
    private String name;
    private double price;
    private int stock;  // 仅用于显示，不直接管理

    public MenuItem(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    // 只在InventoryService中调用此方法
    public void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return String.format("%-20s $%-9.2f Available: %d", name, price, stock);
    }
}