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

    //Constructor
    public ApolongoRepository(Application application){
        ApolongoDB db = ApolongoDB.getDatabase(application);

        mCardDao = db.cardDao();
        mPurchaseDao = db.purchaseDao();

        mAllCards = mCardDao.getAllCards();
        mAllPurchases = mPurchaseDao.getAllPurchases();
    }

    //cards_table operations
    public LiveData<List<Card>> getAllCards(){
        return mAllCards;
    }

    public void insertCard (Card card){
        new insertCardAsyncTask(mCardDao).execute(card);
    }

    public void deleteCard (Card card){
        new deleteCardAsyncTask(mCardDao).execute(card);
    }

    //purchases_table operations
    public LiveData<List<Purchase>> getAllPurchases(){
        return mAllPurchases;
    }

    public void insertPurchase (Purchase purchase){
        new insertPurchaseAsyncTask(mPurchaseDao).execute(purchase);
    }

    public void deletePurchase (Purchase purchase){
        new deletePurchaseAsyncTask(mPurchaseDao).execute(purchase);
    }

    public LiveData<List<Purchase>> getPurchase(String cardName){
        return mPurchaseDao.getPurchase(cardName);
    }

    //This has to be reviewed
    public Purchase getPurchaseByName(String PurchaseName){
        return mPurchaseDao.getPurchaseByName(PurchaseName);
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

    private static class deleteCardAsyncTask extends AsyncTask<Card, Void, Void> {

        private CardDao mAsyncTaskDao;

        deleteCardAsyncTask(CardDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Card... params) {
            mAsyncTaskDao.delete(params[0]);
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

    private static class deletePurchaseAsyncTask extends AsyncTask<Purchase, Void, Void> {

        private PurchaseDao mAsyncTaskDao;

        deletePurchaseAsyncTask(PurchaseDao dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected Void doInBackground(final Purchase... params) {
            mAsyncTaskDao.delete(params[0]);
            return null;
        }
    }
}
