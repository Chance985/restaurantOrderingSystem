package com.Order;

import java.util.Stack;

/**
 * @author Alan 1306198
 * @version 1.0
 */
public class OrderManager {
    private Stack<Order> orderStack;  // Using a stack to manage orders

    public OrderManager() {
        orderStack = new Stack<>();
    }

    // Create a new order and push it onto the stack
    public void createOrder(Order order) {
        orderStack.push(order);
        System.out.println("Order created and added to the stack!");
    }

    // Show the most recent order
    public void showLastOrder() {
        if (!orderStack.isEmpty()) {
            Order lastOrder = orderStack.peek();
            lastOrder.showOrder();
        } else {
            System.out.println("No orders waiting to be processed!");
        }
    }

    // Process the most recent order (LIFO - Last In First Out)
    public void processLastOrder() {
        if (!orderStack.isEmpty()) {
            Order lastOrder = orderStack.pop();
            System.out.println("Processing the order:");
            lastOrder.showOrder();
        } else {
            System.out.println("No orders waiting to be processed!");
        }
    }
}
