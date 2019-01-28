package com.apolongo.apolongo;

import java.util.Date;

public class Cycle {
    private Date mStart;
    private Date mFinish;
    private String mCardName;

    public Cycle(Date start, Date finish, String cardName){
        mStart = start;
        mFinish = finish;
        mCardName = cardName;
    }

    public Date getStart() {
        return mStart;
    }

    public Date getFinish() {
        return mFinish;
    }

    public String getCardName() {return mCardName;}
}
