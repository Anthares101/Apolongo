package com.apolongo.apolongo.DB;

import android.app.Application;
import android.arch.lifecycle.LiveData;
import android.os.AsyncTask;

import java.util.List;

public class ApolongoRepository {
    //cards_table
    private CardDao mCardDao;
    private LiveData<List<Card>> mAllCards;

    //purchases_table
    private PurchaseDao mPurchaseDao;
    private LiveData<List<Purchase>> mAllPurchases;

    ApolongoRepository(Application application){
        ApolongoDB db = ApolongoDB.getDatabase(application);
        mCardDao = db.cardDao();
        mPurchaseDao = db.purchaseDao();
    }

    //cards_table operations
    LiveData<List<Card>> getAllCards(){
        return mAllCards;
    }

    public void insertCard (Card card){
        new insertCardAsyncTask(mCardDao).execute(card);
    }

    //purchases_table operations
    LiveData<List<Purchase>> getAllPurchases(){
        return mAllPurchases;
    }

    public void insertPurchase (Purchase purchase){
        new insertPurchaseAsyncTask(mPurchaseDao).execute(purchase);
    }

    //Async tasks

    //cards_table operations
    private static class insertCardAsyncTask extends AsyncTask<Card, Void, Void> {

        private CardDao mAsyncTaskDao;

        insertCardAsyncTask(CardDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Card... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }

    //purchases_table operations
    private static class insertPurchaseAsyncTask extends AsyncTask<Purchase, Void, Void> {

        private PurchaseDao mAsyncTaskDao;

        insertPurchaseAsyncTask(PurchaseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Purchase... params) {
            mAsyncTaskDao.insert(params[0]);
            return null;
        }
    }
}
