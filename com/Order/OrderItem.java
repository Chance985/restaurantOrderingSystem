package com.Order;

import com.Menu.MenuItem;

/**
 * @author Alan 1306198
 * @version 1.0
 */
public class OrderItem {
    private MenuItem menuItem;  // Menu item
    private int quantity;       // Quantity of the item

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public MenuItem getMenuItem() {
        return menuItem;
    }

    public int getQuantity() {
        return quantity;
    }

    public double getTotalPrice() {
        return menuItem.getPrice() * quantity;  // Total price for this item
    }

    @Override
    public String toString() {
        return menuItem.getName() + " x " + quantity + " = " + getTotalPrice() + " USD";
    }
}
