package com.Inventory;

import com.Menu.MenuItem;
import com.Menu.MenuService;
import java.io.*;
import java.util.*;

/**
 * @author Chance Mo 1306153
 * @version 1.0
 */
public class InventoryService {
    private final MenuService menuService;
    private static final int LOW_STOCK_THRESHOLD = 5;  // 库存警戒线阈值
    private static final String INVENTORY_FILE = "inventory.dat";  // 库存数据文件路径
    private Map<String, InventoryRecord> inventoryRecords;  // 库存记录映射
    private static InventoryService instance;  // 单例模式实例

    // 私有构造函数，确保只能通过getInstance方法获取实例
    private InventoryService(MenuService menuService) {
        this.menuService = menuService;
        this.inventoryRecords = new HashMap<>();
        loadInventoryRecords();
    }

    // 获取InventoryService实例
    public static synchronized InventoryService getInstance(MenuService menuService) {
        if (instance == null) {
            instance = new InventoryService(menuService);
        }
        return instance;
    }

    // 内部类：库存记录
    private static class InventoryRecord implements Serializable {
        private int currentStock;     // 当前库存
        private int reorderPoint;     // 补货点
        private int maxStock;         // 最大库存
        private List<InventoryTransaction> transactions;  // 交易记录列表

        public InventoryRecord(int currentStock) {
            this.currentStock = currentStock;
            this.reorderPoint = LOW_STOCK_THRESHOLD;
            this.maxStock = currentStock * 2;
            this.transactions = new ArrayList<>();
        }
    }

    // 内部类：库存交易记录
    private static class InventoryTransaction implements Serializable {
        private final String type;      // 交易类型："SALE"销售, "RESTOCK"补货
        private final int quantity;     // 交易数量
        private final long timestamp;   // 交易时间戳

        public InventoryTransaction(String type, int quantity) {
            this.type = type;
            this.quantity = quantity;
            this.timestamp = System.currentTimeMillis();
        }
    }

    // 检查并更新库存
    public boolean updateStock(String itemName, int quantity, String operationType) throws InventoryException {
        MenuItem item = menuService.getMenuItem(itemName);
        if (item == null) {
            throw new InventoryException("Item not found: " + itemName);
        }

        // 获取或创建库存记录
        InventoryRecord record = inventoryRecords.computeIfAbsent(
                itemName,
                k -> new InventoryRecord(item.getStock())
        );

        // 根据操作类型更新库存
        if (operationType.equals("SALE")) {
            if (record.currentStock < quantity) {
                throw new InventoryException("Insufficient stock for " + itemName);
            }
            record.currentStock -= quantity;
        } else if (operationType.equals("RESTOCK")) {
            record.currentStock += quantity;
        }

        // 更新MenuItem中的库存显示
        item.setStock(record.currentStock);

        // 记录交易
        record.transactions.add(new InventoryTransaction(operationType,
                operationType.equals("SALE") ? -quantity : quantity));

        // 检查库存警戒线
        checkLowStock(itemName, record);
        saveInventoryRecords();
        return true;
    }

    // 检查库存是否充足
    public boolean checkStock(String itemName, int quantity) {
        MenuItem item = menuService.getMenuItem(itemName);
        if (item == null) return false;

        InventoryRecord record = inventoryRecords.get(itemName);
        if (record == null) {
            record = new InventoryRecord(item.getStock());
            inventoryRecords.put(itemName, record);
        }
        return record.currentStock >= quantity;
    }

    // 检查低库存并发出警告
    private void checkLowStock(String itemName, InventoryRecord record) {
        if (record.currentStock <= record.reorderPoint) {
            System.out.println("\nWarning: Low stock alert for " + itemName + "!");
            System.out.println("Current stock: " + record.currentStock);
            System.out.println("Recommended restock to: " + record.maxStock + " units");
        }
    }

    // 补充库存
    public void restockItem(String itemName, int quantity) throws InventoryException {
        updateStock(itemName, quantity, "RESTOCK");
        InventoryRecord record = inventoryRecords.get(itemName);
        System.out.println("Successfully restocked " + itemName + ". New stock level: " + record.currentStock);
    }

    // 生成库存报告
    public void generateInventoryReport() {
        System.out.println("\n=== Inventory Status Report ===");
        System.out.printf("%-20s %-12s %-12s %-12s%n",
                "Item Name", "Current Stock", "Reorder Point", "Max Stock");
        System.out.println("================================================");

        for (Map.Entry<String, InventoryRecord> entry : inventoryRecords.entrySet()) {
            String itemName = entry.getKey();
            InventoryRecord record = entry.getValue();
            System.out.printf("%-20s %-12d %-12d %-12d%n",
                    itemName,
                    record.currentStock,
                    record.reorderPoint,
                    record.maxStock
            );
        }
    }

    // 查看指定商品的库存历史
    public void viewItemHistory(String itemName) {
        InventoryRecord record = inventoryRecords.get(itemName);
        if (record == null) {
            System.out.println("No inventory record found for: " + itemName);
            return;
        }

        System.out.println("\n=== Inventory History for " + itemName + " ===");
        for (InventoryTransaction transaction : record.transactions) {
            System.out.printf("%s: %d units (Time: %s)%n",
                    transaction.type,
                    transaction.quantity,
                    new Date(transaction.timestamp)
            );
        }
    }

    // 设置库存警戒线
    public void setReorderPoint(String itemName, int reorderPoint) throws InventoryException {
        MenuItem item = menuService.getMenuItem(itemName);
        if (item == null) {
            throw new InventoryException("Item not found: " + itemName);
        }

        InventoryRecord record = inventoryRecords.get(itemName);
        if (record == null) {
            record = new InventoryRecord(item.getStock());
            inventoryRecords.put(itemName, record);
        }

        record.reorderPoint = reorderPoint;
        saveInventoryRecords();
        System.out.println("Successfully updated reorder point for " + itemName + " to " + reorderPoint);
    }

    // 保存库存记录到文件
    private void saveInventoryRecords() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(INVENTORY_FILE))) {
            oos.writeObject(inventoryRecords);
        } catch (IOException e) {
            System.err.println("Error saving inventory records: " + e.getMessage());
        }
    }

    // 从文件加载库存记录
    @SuppressWarnings("unchecked")
    private void loadInventoryRecords() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(INVENTORY_FILE))) {
            inventoryRecords = (Map<String, InventoryRecord>) ois.readObject();
        } catch (FileNotFoundException e) {
            // 文件不存在时使用空的记录映射
            inventoryRecords = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading inventory records: " + e.getMessage());
            inventoryRecords = new HashMap<>();
        }
    }
}