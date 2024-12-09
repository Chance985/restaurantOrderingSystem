package com.Order;

import com.Menu.MenuItem;
import java.io.Serializable;

public class OrderItem implements Serializable {
    private final MenuItem item;
    private final int quantity;

    public OrderItem(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public MenuItem getItem() {
        return item;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return String.format("%s x%d - $%.2f",
                item.getName(),
                quantity,
                item.getPrice() * quantity);
    }
}