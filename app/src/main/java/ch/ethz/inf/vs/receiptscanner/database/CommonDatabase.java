package ch.ethz.inf.vs.receiptscanner.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import java.util.List;

import ch.ethz.inf.vs.receiptscanner.FoodInformation;
import ch.ethz.inf.vs.receiptscanner.expenses.Expense;
import ch.ethz.inf.vs.receiptscanner.inventory.InventoryItem;

@Database(entities = {InventoryItem.class, Expense.class}, version = 1)
public abstract class CommonDatabase extends RoomDatabase {
    public abstract InventoryDao inventoryDao();

    public abstract ExpenseDao expenseDao();

    private static CommonDatabase database;

    public static void initalizeDatabase(Context applicationContext) {
        database = Room.databaseBuilder(applicationContext,
                CommonDatabase.class, "database-common").allowMainThreadQueries().build();
    }

    public static List<InventoryItem> getInventory() {
        return database.inventoryDao().getAll();
    }

    public static List<Expense> getExpenses() {
        return database.expenseDao().getAll();
    }

    public static void addInventoryItem(String purchaseDate, String name) {
        InventoryItem i = new InventoryItem();
        i.purchaseDate = purchaseDate;
        i.expiryDate = FoodInformation.calculateExpiryDate(name, purchaseDate);
        i.name = name;
        database.inventoryDao().insertAll(i);
    }

    public static void addInventoryItems(String[] purchaseDates, String[] names) {
        InventoryItem[] items = new InventoryItem[purchaseDates.length];
        for (int i = 0; i < purchaseDates.length; i++) {
            InventoryItem item = new InventoryItem();
            item.purchaseDate = purchaseDates[i];
            item.expiryDate = FoodInformation.calculateExpiryDate(names[i], purchaseDates[i]);
            item.name = names[i];
            items[i] = item;
        }
        database.inventoryDao().insertAll(items);
    }

    public static void updateInventoryItem(InventoryItem item) {
        database.inventoryDao().update(item);
    }

    public static void addExpense(String date, double price) {
        Expense e = new Expense();
        e.date = date;
        e.price = price;
        database.expenseDao().insertAll(e);
    }

    public static void deleteExpense(Expense expense) {
        database.expenseDao().delete(expense);
    }

    public static void nukeTables() {
        database.inventoryDao().nukeTable();
        database.expenseDao().nukeTable();
    }
}
