package com.apolongo.apolongo.DB;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;
import android.text.format.DateFormat;

@Entity(tableName = "purchase_table")
public class Purchase {
    
    //Private attributes declaration
    @PrimaryKey
    @ColumnInfo(name = "purchase_name")
    private String mPurchaseName;
    @NonNull
    @ColumnInfo(name = "purchase_date")
    private DateFormat mPurchaseDate;
    @NonNull
    @ColumnInfo(name = "purchase_price")
    private Float mPurchasePrice;
    @NonNull
    @ColumnInfo(name = "purchase_sdescp")
    private String mPurchaseSDescp; //Short description of the purchase

    //Constructor
    public Purchase(@NonNull String PurchaseName, @NonNull DateFormat PurchaseDate,
                    @NonNull Float PurchasePrice, @NonNull String PurchaseSDescp){
        this.mPurchaseName = PurchaseName;
        this.mPurchaseDate = PurchaseDate;
        this.mPurchasePrice = PurchasePrice;
        this.mPurchaseSDescp = PurchaseSDescp; //Short description is gonna be optional
    }

    //Getters
    public DateFormat getPruchaseDate() {return mPurchaseDate;}
    public String getPurchaseName(){return mPurchaseName;}
    public Float getPurchasePrice(){return mPurchasePrice;}
    public String getPurchaseSDescp() {return mPurchaseSDescp;}

    //Setters
    public void setPurchaseName(String PurchaseName) {this.mPurchaseName = PurchaseName;}
    public void setPurchaseDate(DateFormat PurchaseDate) {this.mPurchaseDate = PurchaseDate;}
    public void setPurchasePrice(Float PurchasePrice) {this.mPurchasePrice = PurchasePrice;}
    public void setPurchaseSDescp(String PurchaseDescp) {this.mPurchaseSDescp = PurchaseDescp;}
}
