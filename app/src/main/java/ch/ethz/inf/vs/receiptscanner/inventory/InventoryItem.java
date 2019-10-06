package ch.ethz.inf.vs.receiptscanner.inventory;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class InventoryItem {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "item_purchase_date")
    public String purchaseDate;

    @ColumnInfo(name = "item_expiry_date")
    public String expiryDate;

    @ColumnInfo(name = "item_name")
    public String name;
}
