package com.Menu;

/**
 * @author Chance Mo 1306153
 * @version 1.0
 */
public interface MenuItemInterface {
    String getName();

    double getPrice();

    int getStock();

    void updateStock(int quantity);
}
