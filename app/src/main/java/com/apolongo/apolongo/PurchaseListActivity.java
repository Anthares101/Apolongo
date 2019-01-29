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
import android.widget.TextView;
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
    private TextView mTotalPrice;
    private Date mStartDate;
    private Date mFinishDate;
    private String mCardName;
    public static final int NEW_PURCHASE_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_PURCHASE_ACTIVITY_REQUEST_CODE = 2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mApolongoViewModel = ViewModelProviders.of(this).get(ApolongoViewModel.class);
        final PurchaseListAdapter adapter = new PurchaseListAdapter(this, mApolongoViewModel);
        mTotalPrice = findViewById(R.id.total_purchase_value);

        Intent intent = getIntent();
        mStartDate = (Date)intent.getSerializableExtra("startDate");
        mFinishDate = (Date)intent.getSerializableExtra("finishDate");
        mCardName = intent.getStringExtra("cardName");

        mApolongoViewModel.getPurchasesFromCycle(mStartDate, mFinishDate, mCardName).observe(this, new Observer<List<Purchase>>() {
            @Override
            public void onChanged(@Nullable List<Purchase> purchases) {
                //Set the total purchases value
                String totalValue = Float.toString(mApolongoViewModel.getTotalPriceFromCycle(mStartDate, mFinishDate, mCardName));
                String content = "Total " + totalValue + "€";
                mTotalPrice.setText(content);

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

        switch (requestCode){
            case NEW_PURCHASE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK){
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
                    Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
                }

                break;
            case UPDATE_PURCHASE_ACTIVITY_REQUEST_CODE:
                if (resultCode == RESULT_OK){
                    DateFormat format = new SimpleDateFormat("dd / MM / yyyy", Locale.ENGLISH);

                    //Get the data from PurchaseActivity
                    int purchaseId = data.getIntExtra("purchaseId", 0);
                    String purchaseName = data.getStringExtra("purchaseName");
                    Date purchaseDate = null;
                    try {
                        purchaseDate = format.parse(data.getStringExtra("purchaseDate"));
                    }catch (ParseException e) {
                        e.printStackTrace();
                    }
                    float purchasePrice = Float.parseFloat(data.getStringExtra("purchasePrice"));
                    String purchaseDesc = data.getStringExtra("purchaseDesc");

                    //Update the purchase in the database
                    Purchase purchase = new Purchase(purchaseName, purchaseDate, purchasePrice, purchaseDesc, mCardName);
                    purchase.setPurchaseId(purchaseId);
                    mApolongoViewModel.updatePurchase(purchase);

                    Toast.makeText(getApplicationContext(), R.string.saved, Toast.LENGTH_LONG).show();
                }
                break;
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
