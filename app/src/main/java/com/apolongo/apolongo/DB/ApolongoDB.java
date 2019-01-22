package com.apolongo.apolongo.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {Card.class, Purchase.class}, version = 1)
public abstract class ApolongoDB extends RoomDatabase {
    public abstract CardDao cardDao();
    public abstract PurchaDao purchaseDao();
}
