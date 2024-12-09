package com.Inventory;

import com.Menu.MenuItem;
import com.Menu.MenuService;

import java.io.*;
import java.util.*;

public class InventoryService {
    private final MenuService menuService;
    private static final int LOW_STOCK_THRESHOLD = 5;
    private static final String INVENTORY_FILE = "inventory.dat";
    private Map<String, InventoryRecord> inventoryRecords;
    private static InventoryService instance;
    private Timer inventoryCheckTimer;
    private static final long INVENTORY_CHECK_INTERVAL = 3600000; // 1小时检查一次

    private InventoryService(MenuService menuService) {
        this.menuService = menuService;
        this.inventoryRecords = new HashMap<>();
        loadInventoryRecords();
        initializeInventoryChecker();
    }

    public static synchronized InventoryService getInstance(MenuService menuService) {
        if (instance == null) {
            instance = new InventoryService(menuService);
        }
        return instance;
    }

    private static class InventoryRecord implements Serializable {
        private int currentStock;
        private int reorderPoint;
        private int maxStock;
        private List<InventoryTransaction> transactions;

        public InventoryRecord(int currentStock) {
            this.currentStock = currentStock;
            this.reorderPoint = LOW_STOCK_THRESHOLD;
            this.maxStock = currentStock * 2;
            this.transactions = new ArrayList<>();
        }
    }

    private static class InventoryTransaction implements Serializable {
        private final String type;
        private final int quantity;
        private final long timestamp;

        public InventoryTransaction(String type, int quantity) {
            this.type = type;
            this.quantity = quantity;
            this.timestamp = System.currentTimeMillis();
        }
    }

    private void initializeInventoryChecker() {
        inventoryCheckTimer = new Timer(true);
        inventoryCheckTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                checkAllInventoryLevels();
            }
        }, INVENTORY_CHECK_INTERVAL, INVENTORY_CHECK_INTERVAL);
    }

    private void checkAllInventoryLevels() {
        System.out.println("\n=== Scheduled Inventory Check ===");
        for (Map.Entry<String, InventoryRecord> entry : inventoryRecords.entrySet()) {
            String itemName = entry.getKey();
            InventoryRecord record = entry.getValue();
            checkLowStock(itemName, record);
        }
    }

    public synchronized boolean updateStock(String itemName, int quantity, String operationType) throws InventoryException {
        MenuItem item = menuService.getMenuItem(itemName);
        if (item == null) {
            throw new InventoryException("Item not found: " + itemName);
        }

        InventoryRecord record = inventoryRecords.computeIfAbsent(
                itemName,
                k -> new InventoryRecord(item.getStock())
        );

        int newStock;
        if (operationType.equals("SALE")) {
            if (record.currentStock < quantity) {
                throw new InventoryException("Insufficient stock for " + itemName);
            }
            newStock = record.currentStock - quantity;
        } else if (operationType.equals("RESTOCK")) {
            newStock = record.currentStock + quantity;
        } else {
            throw new InventoryException("Invalid operation type: " + operationType);
        }

        record.currentStock = newStock;
        item.updateStock(newStock);
        menuService.syncMenuStock(itemName, newStock);

        record.transactions.add(new InventoryTransaction(operationType,
                operationType.equals("SALE") ? -quantity : quantity));

        checkLowStock(itemName, record);
        saveInventoryRecords();

        System.out.println("Stock updated for " + itemName + ": " + newStock + " units remaining");

        return true;
    }

    public synchronized boolean checkStock(String itemName, int quantity) {
        MenuItem item = menuService.getMenuItem(itemName);
        if (item == null) return false;

        InventoryRecord record = inventoryRecords.get(itemName);
        if (record == null) {
            record = new InventoryRecord(item.getStock());
            inventoryRecords.put(itemName, record);
        }
        return record.currentStock >= quantity;
    }

    public synchronized int getCurrentStock(String itemName) {
        InventoryRecord record = inventoryRecords.get(itemName);
        return record != null ? record.currentStock : 0;
    }

    private void checkLowStock(String itemName, InventoryRecord record) {
        if (record.currentStock <= record.reorderPoint) {
            System.out.println("\nWarning: Low stock alert for " + itemName + "!");
            System.out.println("Current stock: " + record.currentStock);
            System.out.println("Recommended restock to: " + record.maxStock + " units");
        }
    }

    public void restockItem(String itemName, int quantity) throws InventoryException {
        updateStock(itemName, quantity, "RESTOCK");
        InventoryRecord record = inventoryRecords.get(itemName);
        System.out.println("Successfully restocked " + itemName + ". New stock level: " + record.currentStock);
    }

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

    private void saveInventoryRecords() {
        try (ObjectOutputStream oos = new ObjectOutputStream(
                new FileOutputStream(INVENTORY_FILE))) {
            oos.writeObject(inventoryRecords);
        } catch (IOException e) {
            System.err.println("Error saving inventory records: " + e.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private void loadInventoryRecords() {
        try (ObjectInputStream ois = new ObjectInputStream(
                new FileInputStream(INVENTORY_FILE))) {
            inventoryRecords = (Map<String, InventoryRecord>) ois.readObject();

            for (Map.Entry<String, InventoryRecord> entry : inventoryRecords.entrySet()) {
                String itemName = entry.getKey();
                int currentStock = entry.getValue().currentStock;
                MenuItem item = menuService.getMenuItem(itemName);
                if (item != null) {
                    item.updateStock(currentStock);
                }
            }
        } catch (FileNotFoundException e) {
            inventoryRecords = new HashMap<>();
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading inventory records: " + e.getMessage());
            inventoryRecords = new HashMap<>();
        }
    }

    public void shutdown() {
        if (inventoryCheckTimer != null) {
            inventoryCheckTimer.cancel();
        }
        saveInventoryRecords();
    }
}