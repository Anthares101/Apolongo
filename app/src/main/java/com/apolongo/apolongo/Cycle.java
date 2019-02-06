package com.apolongo.apolongo;

import java.util.Date;

public class Cycle {
    private Date mStart;
    private Date mFinish;
    private int mCardId;

    public Cycle(Date start, Date finish, int cardName){
        mStart = start;
        mFinish = finish;
        mCardId = cardName;
    }

    public Date getStart() {
        return mStart;
    }

    public Date getFinish() {
        return mFinish;
    }

    public int getCardId() {return mCardId;}
}
