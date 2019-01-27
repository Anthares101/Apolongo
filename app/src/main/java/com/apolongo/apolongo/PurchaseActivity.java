package com.apolongo.apolongo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
        import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.widget.EditText;
        import android.widget.TextSwitcher;
        import android.widget.TextView;

import com.apolongo.apolongo.DB.Card;
import com.apolongo.apolongo.DB.Purchase;

import java.util.List;

public class PurchaseActivity extends AppCompatActivity {

    private TextView mNameEditable;
    private Purchase mPurchase;
    private ApolongoViewModel mApolongoViewModel;
    private int mPurchaseId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        Intent intent = getIntent();
        mPurchaseId = Integer.parseInt(intent.getStringExtra("PurchaseId"));

        mApolongoViewModel = ViewModelProviders.of(this).get(ApolongoViewModel.class);

        mPurchase = mApolongoViewModel.getPurchaseById(mPurchaseId); //It breaks here


        //mNameEditable = findViewById(R.id.NameEditable);
        //mNameEditable.setText(mPurchase.getPurchaseName());

    }
}
