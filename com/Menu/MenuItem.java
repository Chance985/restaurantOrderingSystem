package com.Menu;

import java.io.Serializable;

public class MenuItem implements Serializable, MenuItemInterface {
    private static final long serialVersionUID = -8820186751161460358L;  // 添加这一行

    private String name;
    private double price;
    private volatile int stock;

    public MenuItem(String name, double price, int stock) {
        this.name = name;
        this.price = price;
        this.stock = stock;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

    @Override
    public int getStock() {
        return stock;
    }

    @Override
    public synchronized void updateStock(int quantity) {
        this.stock = quantity;
    }

    public synchronized void setStock(int stock) {
        this.stock = stock;
    }

    @Override
    public String toString() {
        return String.format("%-20s $%-9.2f Available: %d", name, price, stock);
    }
}