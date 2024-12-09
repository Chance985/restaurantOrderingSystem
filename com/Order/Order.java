package com.Order;

import com.Menu.MenuItem;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class Order implements Serializable {
    private static int orderCounter = 1000;
    private final int orderId;
    private final List<OrderItem> items;
    private double totalPrice;
    private OrderStatus status;
    private final long timestamp;

    public Order() {
        this.orderId = ++orderCounter;
        this.items = new ArrayList<>();
        this.totalPrice = 0.0;
        this.status = OrderStatus.PENDING;
        this.timestamp = System.currentTimeMillis();
    }

    public void addItem(MenuItem item, int quantity) {
        items.add(new OrderItem(item, quantity));
        totalPrice += item.getPrice() * quantity;
    }

    public int getOrderId() {
        return orderId;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }

    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Order #").append(orderId).append("\n");
        sb.append("Items:\n");
        for (OrderItem item : items) {
            sb.append(String.format("  %-20s x%-3d $%.2f\n",
                    item.getItem().getName(),
                    item.getQuantity(),
                    item.getItem().getPrice() * item.getQuantity()));
        }
        sb.append(String.format("Total Price: $%.2f\n", totalPrice));
        sb.append("Status: ").append(status);
        return sb.toString();
    }
}