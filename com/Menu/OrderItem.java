package com.Menu;

/**
 * @author Chance Mo 1306153
 * @version 1.0
 */
class OrderItem {
    private MenuItem menuItem;
    private int quantity;

    public OrderItem(MenuItem menuItem, int quantity) {
        this.menuItem = menuItem;
        this.quantity = quantity;
    }

    public double getTotalPrice() {
        return menuItem.getPrice() * quantity;
    }

    public String getName() {
        return menuItem.getName();
    }

    public int getQuantity() {
        return quantity;
    }

    public void reduceStock() {
        menuItem.updateStock(quantity);
    }

    @Override
    public String toString() {
        return quantity + " x " + menuItem.getName() + " = $" + getTotalPrice();
    }
}
