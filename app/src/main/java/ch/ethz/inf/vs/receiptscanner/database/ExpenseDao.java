package ch.ethz.inf.vs.receiptscanner.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import ch.ethz.inf.vs.receiptscanner.expenses.Expense;

@Dao
public interface ExpenseDao {
    @Query("SELECT * FROM expense")
    List<Expense> getAll();

    @Query("SELECT * FROM expense WHERE uid IN (:uIds)")
    List<Expense> loadAllByIds(int[] uIds);

    @Query("SELECT * FROM expense WHERE expense_date LIKE :date LIMIT 1")
    Expense findByDate(String date);

    @Insert
    void insertAll(Expense... expenses);

    @Delete
    void delete(Expense expense);

    @Query("DELETE FROM expense")
    public void nukeTable();



}
