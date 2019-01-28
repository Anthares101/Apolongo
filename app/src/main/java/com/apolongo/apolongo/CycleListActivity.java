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
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CycleListActivity extends AppCompatActivity {
    private ApolongoViewModel mApolongoViewModel;
    public static final int NEW_PURCHASE_ACTIVITY_REQUEST_CODE = 1;
    private String mCardName;
    private int mCycle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cycle_list);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mCardName = intent.getStringExtra("cardName");
        mCycle = intent.getIntExtra("cardCycle",1);

        mApolongoViewModel = ViewModelProviders.of(this).get(ApolongoViewModel.class);
        final CycleListAdapter adapter = new CycleListAdapter(this, mApolongoViewModel);

        mApolongoViewModel.getPurchases(mCardName).observe(this, new Observer<List<Purchase>>() {
            @Override
            public void onChanged(@Nullable List<Purchase> purchases) {
                List<Cycle> cycles = new ArrayList<>();

                for(int i=0; i < purchases.size(); i++){
                    Cycle cycle;
                    Date start = null;
                    Date finish = null;
                    DateFormat format = new SimpleDateFormat("dd / MM / yyyy", Locale.ENGLISH);

                    //Get the i purchase from the list
                    Purchase purchase = purchases.get(i);
                    //Get the purchase date
                    Date purchaseDate = purchase.getPurchaseDate();
                    //Split the date
                    String[] date = format.format(purchaseDate).split("/");
                    String day = date[0].replaceAll(" ","");
                    String month = date[1].replaceAll(" ", "");
                    String year = date[2].replaceAll(" ", "");

                    //Cycles are calculated based on the purchase date
                    int dayInt = mCycle - 1;
                    int monthInt = Integer.parseInt(month);
                    int yearInt = Integer.parseInt(year);

                    //If the cycles start the day 1, the last day of the month must be calculated
                    if(dayInt < 1){
                        if(monthInt == 2) {
                            if(yearInt % 4 == 0)
                                dayInt = 29;
                            else
                                dayInt = 28;
                        }
                        else{
                            if(monthInt == 4 || monthInt == 6 || monthInt == 9 || monthInt == 11){
                                dayInt = 30;
                            }
                            else
                                dayInt = 31;
                        }

                        monthInt--; //Will be incremented after
                    }

                    if(Integer.parseInt(day) >= mCycle) {//Purchase date newer than billing cycle
                        monthInt++;
                        if (monthInt > 12) {
                            monthInt = 1;
                            yearInt++;
                        }

                        try {
                            start = format.parse(Integer.toString(mCycle) + " /" + date[1] + "/" + date[2]);
                            finish = format.parse(Integer.toString(dayInt) + " / " + Integer.toString(monthInt) + " / " + Integer.toString(yearInt));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }
                    else{//Purchase date older than billing cycle
                        monthInt--;
                        if(monthInt < 1){
                            monthInt = 12;
                            yearInt--;
                        }

                        try {
                            start = format.parse(Integer.toString(mCycle) + " / " + Integer.toString(monthInt) + " / " + Integer.toString(yearInt));
                            finish = format.parse(Integer.toString(dayInt) + " /" + date[1] + "/" + date[2]);
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }


                    cycle = new Cycle(start, finish, mCardName);
                    //The calculated cycle is added to the cycle list if the list doesn't contain it already
                    int j = 0;
                    while(j < cycles.size() && cycles.get(j).getStart().compareTo(cycle.getStart()) != 0)
                        j++;
                    if(j == cycles.size())
                        cycles.add(cycle);
                }
                //Update the cached copy of the cycles in the adapter
                adapter.setCycles(cycles);
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
