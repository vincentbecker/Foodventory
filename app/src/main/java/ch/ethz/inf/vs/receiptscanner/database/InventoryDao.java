package ch.ethz.inf.vs.receiptscanner.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import ch.ethz.inf.vs.receiptscanner.inventory.InventoryItem;

@Dao
public interface InventoryDao {
    @Query("SELECT * FROM inventoryitem")
    List<InventoryItem> getAll();

    @Query("SELECT * FROM inventoryitem WHERE uid IN (:uIds)")
    List<InventoryItem> loadAllByIds(int[] uIds);

    @Query("SELECT * FROM inventoryitem WHERE item_purchase_date LIKE :date LIMIT 1")
    InventoryItem findByDate(String date);

    @Insert
    void insertAll(InventoryItem... inventoryItems);

    @Update
    void update(InventoryItem inventoryItem);

    @Delete
    void delete(InventoryItem inventoryItem);

    @Query("DELETE FROM inventoryitem")
    public void nukeTable();



}
