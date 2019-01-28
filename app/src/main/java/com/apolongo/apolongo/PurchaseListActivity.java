package com.apolongo.apolongo;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.apolongo.apolongo.DB.Purchase;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PurchaseListActivity extends AppCompatActivity {

    private ApolongoViewModel mApolongoViewModel;
    private Date mStartDate;
    private Date mFinishDate;
    private String mCardName;
    public static final int NEW_PURCHASE_ACTIVITY_REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mApolongoViewModel = ViewModelProviders.of(this).get(ApolongoViewModel.class);
        final PurchaseListAdapter adapter = new PurchaseListAdapter(this, mApolongoViewModel);

        Intent intent = getIntent();
        mStartDate = (Date)intent.getSerializableExtra("startDate");
        mFinishDate = (Date)intent.getSerializableExtra("finishDate");
        mCardName = intent.getStringExtra("cardName");

        mApolongoViewModel.getPurchasesFromCycle(mStartDate, mFinishDate, mCardName).observe(this, new Observer<List<Purchase>>() {
            @Override
            public void onChanged(@Nullable List<Purchase> purchases) {
                //Update the cached copy of the purchases in the adapter
                adapter.setPurchases(purchases);
            }
        });

        //Add RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.newPurchaseButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewPurchase = new Intent(getApplicationContext(), NewPurchaseActivity.class);
                startActivityForResult(NewPurchase, NEW_PURCHASE_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    //NewPurchaseActivity Result observer
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_PURCHASE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            String string = data.getStringExtra("date");
            DateFormat format = new SimpleDateFormat("dd / MM / yyyy", Locale.ENGLISH); //Pattern MUST be controlled in the activity_new_purchase
            Date date = null;
            try {
                date = format.parse(string);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Purchase purchase = new Purchase(data.getStringExtra("name"),
                    date,
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
