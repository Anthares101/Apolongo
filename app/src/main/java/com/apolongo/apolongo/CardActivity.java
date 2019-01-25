package com.apolongo.apolongo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.apolongo.apolongo.DB.Purchase;

import java.util.List;

public class CardActivity extends AppCompatActivity {
    private ApolongoViewModel mApolongoViewModel;
    public static final int NEW_PURCHASE_ACTIVITY_REQUEST_CODE = 1;
    private String mCardName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        String cardName = intent.getStringExtra("cardName");
        mCardName = cardName;

        mApolongoViewModel = ViewModelProviders.of(this).get(ApolongoViewModel.class);
        final CycleListAdapter adapter = new CycleListAdapter(this, mApolongoViewModel);

        mApolongoViewModel.getAllPurchasess().observe(this, new Observer<List<Purchase>>() {
            @Override
            public void onChanged(@Nullable List<Purchase> purchases) {
                //Update the cached copy of the purchases in the adapter
                adapter.setPurchases(purchases);
            }
        });

        //Add RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton newPurchaseButton = (FloatingActionButton) findViewById(R.id.newPurchaseButton);
        newPurchaseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewPurchase = new Intent(getApplicationContext(), NewPurchaseActivity.class);
                startActivityForResult(NewPurchase, NEW_PURCHASE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    //NewCardActivity Result observer
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_PURCHASE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Purchase purchase = new Purchase(data.getStringExtra("name"),
                                             data.getStringExtra("date"),
                                             Float.parseFloat(data.getStringExtra("price")),
                                            "No desc", //This will be as name, date and price
                                             mCardName);
            mApolongoViewModel.insertPurchase(purchase);
        } else {
            Toast.makeText(getApplicationContext(),R.string.empty_not_saved, Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
