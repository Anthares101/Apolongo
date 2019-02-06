package com.apolongo.apolongo.DB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "cards_table")
public class Card {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "card_id")
    private int mCardId;
    @NonNull
    @ColumnInfo(name = "card_name")
    private String mCardName;
    @NonNull
    @ColumnInfo(name = "card_image")
    private String mImageName;
    @NonNull
    @ColumnInfo(name = "card_cycle")
    private int mBillingCycle;

    public Card(@NonNull String mCardName, @NonNull String mImageName, @NonNull int mBillingCycle){
        this.mCardId = 0;
        this.mCardName = mCardName;
        this.mImageName = mImageName;
        this.mBillingCycle = mBillingCycle;
    }

    public int getCardId(){return mCardId;}
    public String getCardName(){return mCardName;}
    public String getImageName(){return mImageName;}
    public int getBillingCycle(){return mBillingCycle;}

    public void setCardId(int cardId){this.mCardId = cardId;}
    public void setCardName(String cardName) {
        this.mCardName = cardName;
    }
    public void setImageName(String imageName) {
        this.mImageName = imageName;
    }
    public void setBillingCycle(int cycle) {
        this.mBillingCycle = cycle;
    }
}