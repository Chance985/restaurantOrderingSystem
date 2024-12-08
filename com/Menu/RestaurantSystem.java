package com.Menu;

/**
 * @author Chance Mo 1306153
 * @version 1.0
 */
public class RestaurantSystem {
    public static void main(String[] args) {
        MenuService menuService = new MenuService();
        menuService.addMenuItem(new MenuItem("Burger", 10.0, 50));
        menuService.addMenuItem(new MenuItem("Pizza", 12.5, 30));
        menuService.addMenuItem(new MenuItem("Pasta", 8.0, 20));
//
//        OrderService orderService = new OrderService();
//
//        // 创建订单
//        Order order = orderService.createOrder(menuService);
//        orderService.processPayment(order);
//
//        // 保存订单和菜单
//        FileUtils.saveMenuToFile(new ArrayList<>(menuService.getMenuItems().values()));
//        FileUtils.saveOrderToFile(order);
//
//        // 加载菜单和订单
//        menuService.displayMenu();
//        Order loadedOrder = FileUtils.loadOrderFromFile(order.hashCode());
//        if (loadedOrder != null) {
//            System.out.println("Loaded Order: " + loadedOrder);
//        }
    }
}