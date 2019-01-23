package com.apolongo.apolongo.DB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity(tableName = "cards_table")
public class Card {
    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "card_name")
    private String mCardName;
    @NonNull
    @ColumnInfo(name = "card_image")
    private String mImageName;
    @NonNull
    @ColumnInfo(name = "card_cycle")
    private int mBillingCycle;

    public Card(@NonNull String card, @NonNull String image, @NonNull int cycle){
        this.mCardName = card;
        this.mImageName = image;
        this.mBillingCycle = cycle;
    }

    public String getCardName(){return mCardName;}
    public String getImageName(){return mImageName;}
    public int getBillingCycle(){return mBillingCycle;}

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