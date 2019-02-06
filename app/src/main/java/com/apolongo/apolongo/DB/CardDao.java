package com.apolongo.apolongo.DB;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import static android.arch.persistence.room.OnConflictStrategy.REPLACE;

@Dao
public interface CardDao {
    @Insert
    void insert(Card card);

    @Update(onConflict = REPLACE)
    void update(Card card);

    @Delete
    void delete(Card card);

    @Query("DELETE FROM cards_table")
    void deleteAll();

    @Query("SELECT * from cards_table ORDER BY card_name ASC")
    LiveData<List<Card>> getAllCards();

    @Query("SELECT * from cards_table WHERE card_name = :cardName")
    Card getCardByName(String cardName);

    @Query("SELECT COUNT(card_name) FROM cards_table WHERE card_name=:cardName")
    int alreadyExist(String cardName);
}
