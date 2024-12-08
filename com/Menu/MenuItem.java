package com.Menu;

import java.io.Serializable;

/**
 * @author Chance Mo 1306153
 * @version 1.0
 */
// 父类 MenuItem
public class MenuItem implements MenuItemInterface, Serializable {
    private String name;
    private double price;
    private int stock;

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
    public void updateStock(int quantity) {
        this.stock -= quantity;
    }

    @Override
    public String toString() {
        return name + " - $" + price + " - Stock: " + stock;
    }
}


