package com.apolongo.apolongo.DB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.Date;
import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface PurchaseDao {
    @Insert
    void insert(Purchase purchase);

    @Update(onConflict = REPLACE)
    void update(Purchase purchase);

    @Delete
    void delete(Purchase purchase);

    @Query("DELETE FROM purchases_table")
    void deleteAll();

    @Query("DELETE FROM purchases_table WHERE purchase_date >= :startDate and purchase_date <= :finishDate and purchase_cardId = :cardId")
    void deletePurchasesFromCycle(Date startDate, Date finishDate, int cardId);

    @Query("SELECT * FROM purchases_table ORDER BY purchase_date DESC")
    LiveData<List<Purchase>> getAllPurchases();

    @Query("SELECT * FROM purchases_table WHERE purchase_cardId = :cardId ORDER BY purchase_date DESC")
    LiveData<List<Purchase>> getPurchases(int cardId);

    @Query("SELECT * FROM purchases_table WHERE purchase_id = :purchaseId")
    Purchase getPurchaseById(int purchaseId);

    @Query("SELECT * FROM purchases_table WHERE purchase_date >= :startDate and purchase_date <= :finishDate and purchase_cardId = :cardId")
    LiveData<List<Purchase>> getPurchasesFromCycle(Date startDate, Date finishDate, int cardId);

    @Query("SELECT SUM(purchase_price) FROM purchases_table WHERE purchase_date >= :startDate and purchase_date <= :finishDate and purchase_cardId = :cardId")
    float getTotalPriceFromCycle(Date startDate, Date finishDate, int cardId);
}
