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
    private Cycle mCycle;
    public static final int NEW_PURCHASE_ACTIVITY_REQUEST_CODE = 1;
    public static final int UPDATE_PURCHASE_ACTIVITY_REQUEST_CODE = 2;
    public static final int RESULT_DELETE = -2;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_list);
        mTotalPrice = findViewById(R.id.total_purchase_value);

        //Initializes the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Add a back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        Date startDate = (Date)intent.getSerializableExtra("startDate");
        Date finishDate = (Date)intent.getSerializableExtra("finishDate");
        int cardId = intent.getIntExtra("cardId", 0);
        mCycle = new Cycle(startDate, finishDate, cardId);

        mApolongoViewModel = ViewModelProviders.of(this).get(ApolongoViewModel.class);
        final PurchaseListAdapter adapter = new PurchaseListAdapter(this, mApolongoViewModel);

        mApolongoViewModel.getPurchasesFromCycle(mCycle.getStart(), mCycle.getFinish(), mCycle.getCardId()).observe(this, new Observer<List<Purchase>>() {
            @Override
            public void onChanged(@Nullable List<Purchase> purchases) {
                //Set the total purchases value
                String totalValue = Float.toString(mApolongoViewModel.getTotalPriceFromCycle(mCycle.getStart(), mCycle.getFinish(), mCycle.getCardId()));
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
                    DateFormat format = new SimpleDateFormat("dd / MM / yyyy", Locale.ENGLISH);

                    //Get the data from activity
                    String purchaseName = data.getStringExtra("purchaseName");
                    Date purchaseDate = null;
                    try {
                        purchaseDate = format.parse(data.getStringExtra("purchaseDate"));
                    }catch (ParseException e) {
                        e.printStackTrace();
                    }
                    float purchasePrice = Float.parseFloat(data.getStringExtra("purchasePrice"));
                    String purchaseDesc = data.getStringExtra("purchaseDesc");

                    //Insert the purchase in the database
                    Purchase purchase = new Purchase(purchaseName, purchaseDate, purchasePrice, purchaseDesc, mCycle.getCardId());
                    mApolongoViewModel.insertPurchase(purchase);

                    Toast.makeText(getApplicationContext(), R.string.button_save, Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), R.string.empty_not_saved, Toast.LENGTH_LONG).show();
                }

                break;
            case UPDATE_PURCHASE_ACTIVITY_REQUEST_CODE:
                Purchase purchase;
                int purchaseId;
                String purchaseName;
                Date purchaseDate = null;
                float purchasePrice;
                String purchaseDesc;

                switch (resultCode){
                    case RESULT_OK:
                        DateFormat format = new SimpleDateFormat("dd / MM / yyyy", Locale.ENGLISH);

                        //Get the data from activity
                        purchaseId = data.getIntExtra("purchaseId", 0);
                        purchaseName = data.getStringExtra("purchaseName");
                        try {
                            purchaseDate = format.parse(data.getStringExtra("purchaseDate"));
                        }catch (ParseException e) {
                            e.printStackTrace();
                        }
                        purchasePrice = Float.parseFloat(data.getStringExtra("purchasePrice"));
                        purchaseDesc = data.getStringExtra("purchaseDesc");

                        //Update the purchase in the database
                        purchase = new Purchase(purchaseName, purchaseDate, purchasePrice, purchaseDesc, mCycle.getCardId());
                        purchase.setPurchaseId(purchaseId);
                        mApolongoViewModel.updatePurchase(purchase);

                        Toast.makeText(getApplicationContext(), R.string.saved, Toast.LENGTH_LONG).show();
                        break;
                    case RESULT_DELETE:
                        //Get the data from activity
                        purchaseId = data.getIntExtra("purchaseId", 0);
                        purchaseName = data.getStringExtra("purchaseName");
                        purchaseDate = (Date) data.getSerializableExtra("purchaseDate");
                        purchasePrice = data.getFloatExtra("purchasePrice", 0);
                        purchaseDesc = data.getStringExtra("purchaseDesc");

                        //Delete the purchase in the database
                        purchase = new Purchase(purchaseName, purchaseDate, purchasePrice, purchaseDesc, mCycle.getCardId());
                        purchase.setPurchaseId(purchaseId);
                        mApolongoViewModel.deletePurchase(purchase);

                        Toast.makeText(getApplicationContext(), "Borrada", Toast.LENGTH_LONG).show();
                        break;
                }
                break;
        }
    }

    //Used to manage the ToolBar options (The 3 dots) right now not necessary
    /*@Override
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
    }*/

}
