package com.apolongo.apolongo;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;

import com.apolongo.apolongo.DB.ApolongoRepository;
import com.apolongo.apolongo.DB.Card;
import com.apolongo.apolongo.DB.Purchase;

import java.util.List;

public class ApolongoViewModel extends AndroidViewModel {
    private ApolongoRepository mRepository;

    private LiveData<List<Card>> mAllCards;
    private LiveData<List<Purchase>> mAllPurchases;

    public ApolongoViewModel (Application application) {
        super(application);
        mRepository = new ApolongoRepository(application);

        mAllCards = mRepository.getAllCards();
        mAllPurchases = mRepository.getAllPurchases();
    }

    //Cards operations
    LiveData<List<Card>> getAllCards() { return mAllCards; }
    public void insertCard(Card card) { mRepository.insertCard(card); }
    public void deleteCard(Card card) { mRepository.deleteCard(card); }

    //Purchases operations
    LiveData<List<Purchase>> getAllPurchasess() { return mAllPurchases; }
    public void insertPurchase(Purchase purchase) { mRepository.insertPurchase(purchase); }
    public void deletePurchase(Purchase purchase) { mRepository.deletePurchase(purchase); }
    public LiveData<List<Purchase>> getPurchase(String cardName){return mRepository.getPurchase(cardName);}
}
