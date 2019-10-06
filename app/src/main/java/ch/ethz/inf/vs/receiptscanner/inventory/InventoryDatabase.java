package ch.ethz.inf.vs.receiptscanner.inventory;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import java.util.List;

import ch.ethz.inf.vs.receiptscanner.FoodInformation;
import ch.ethz.inf.vs.receiptscanner.database.InventoryDao;

@Database(entities = {InventoryItem.class}, version = 1)
public abstract class InventoryDatabase extends RoomDatabase {
    public abstract InventoryDao inventoryDao();

    private static InventoryDatabase database;
    private static int uidCount = 1000;

    public static void initalizeDatabase(Context applicationContext) {
        database = Room.databaseBuilder(applicationContext,
                InventoryDatabase.class, "database-inventory").allowMainThreadQueries().build();
    }

    public static List<InventoryItem> getInventory() {
        return database.inventoryDao().getAll();
    }

    public static void addInventoryItem(String purchaseDate, String name) {
        InventoryItem e = new InventoryItem();
        e.uid = uidCount;
        e.purchaseDate = purchaseDate;
        e.expiryDate = FoodInformation.calculateExpiryDate(name, purchaseDate);
        e.name = name;
        database.inventoryDao().insertAll(e);
        uidCount++;
    }

    public static void deleteExpense(InventoryItem item) {
        database.inventoryDao().delete(item);
    }

    public static void nukeTable() {
        database.inventoryDao().nukeTable();
    }
}
