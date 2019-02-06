package com.apolongo.apolongo.DB;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.apolongo.apolongo.DB.Converters.Converters;

@Database(entities = {Card.class, Purchase.class}, version = 6)
@TypeConverters({Converters.class})
public abstract class ApolongoDB extends RoomDatabase {
    public abstract CardDao cardDao();
    public abstract PurchaseDao purchaseDao();

    private static volatile ApolongoDB INSTANCE;

    static ApolongoDB getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (ApolongoDB.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            ApolongoDB.class, "apolongo_database")
                            //.fallbackToDestructiveMigration()//Destroy the DB if the schema changes
                            .build();
                }
            }
        }
        return INSTANCE;
    }
}
