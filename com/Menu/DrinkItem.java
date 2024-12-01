package com.Menu;

/**
 * @author Chance Mo 1306153
 * @version 1.0
 */
// 子类 DrinkItem，继承自 MenuItem
public class DrinkItem extends MenuItem {
    private String size;

    public DrinkItem(String name, double price, int stock, String size) {
        super(name, price, stock);
        this.size = size;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    @Override
    public String toString() {
        return super.toString() + ", size='" + size + "'";
    }
}
