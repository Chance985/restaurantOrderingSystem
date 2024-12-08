package com.Order;

import com.Menu.MenuItem;
import java.util.ArrayList;
import java.util.List;
/**
 * @author Alan 1306198
 * @version 1.0
 */
public class Order {
    private List<OrderItem> orderItems;  // List of items in the order
    private double totalAmount;          // Total amount of the order

    public Order() {
        this.orderItems = new ArrayList<>();
        this.totalAmount = 0.0;
    }

    // Add an order item to the order
    public void addOrderItem(MenuItem menuItem, int quantity) {
        if (menuItem.getStock() < quantity) {
            System.out.println("Insufficient stock! Cannot add this item.");
        } else {
            // Decrease stock
            menuItem.setStock(menuItem.getStock() - quantity);
            if (menuItem.getStock() < 5) {
                System.out.println("Warning! " + menuItem.getName() + " stock is below 5 items!");
            }
            // Create order item and add it to the order
            OrderItem orderItem = new OrderItem(menuItem, quantity);
            orderItems.add(orderItem);
            totalAmount += orderItem.getTotalPrice();  // Update total amount
            System.out.println("Added " + quantity + " x " + menuItem.getName() + " to the order.");
        }
    }

    // Show order details
    public void showOrder() {
        if (orderItems.isEmpty()) {
            System.out.println("The order is empty. Please add items first!");
            return;
        }
        System.out.println("\n--- Order Details ---");
        for (OrderItem item : orderItems) {
            System.out.println(item);
        }
        System.out.println("Total amount: " + totalAmount + " USD");
    }

    // Get the total amount of the order
    public double getTotalAmount() {
        return totalAmount;
    }
}
