package com.apolongo.apolongo.DB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PurchaseDao {
    @Insert
    void insert(Purchase purchase);

    @Delete
    void delete(Purchase purchase);

    @Query("DELETE FROM purchases_table")
    void deleteAll();

    @Query("SELECT * FROM purchases_table ORDER BY purchase_date DESC")
    LiveData<List<Purchase>> getAllPurchases();

    @Query("SELECT * FROM purchases_table WHERE purchase_CardName = :cardName ORDER BY purchase_date DESC")
    LiveData<List<Purchase>> getPurchase(String cardName);

    @Query("SELECT * FROM purchases_table WHERE purchase_id = :purchaseId")
    Purchase getPurchaseById(int purchaseId);
}
