package ch.ethz.inf.vs.receiptscanner.expenses;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Expense {

    @PrimaryKey(autoGenerate = true)
    public int uid;

    @ColumnInfo(name = "expense_date")
    public String date;

    @ColumnInfo(name = "expense_price")
    public double price;

}
