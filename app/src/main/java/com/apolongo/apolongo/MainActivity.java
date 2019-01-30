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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.apolongo.apolongo.DB.Card;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private ApolongoViewModel mApolongoViewModel;
    public static final int NEW_CARD_ACTIVITY_REQUEST_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mApolongoViewModel = ViewModelProviders.of(this).get(ApolongoViewModel.class);
        final CardListAdapter adapter = new CardListAdapter(this, mApolongoViewModel);

        mApolongoViewModel.getAllCards().observe(this, new Observer<List<Card>>() {
            @Override
            public void onChanged(@Nullable List<Card> cards) {
                //Update the cached copy of the cards in the adapter
                adapter.setCards(cards);
            }
        });

        //Add RecyclerView
        RecyclerView recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent NewCard = new Intent(getApplicationContext(), NewCardActivity.class);
                startActivityForResult(NewCard, NEW_CARD_ACTIVITY_REQUEST_CODE);
            }
        });
    }

    //NewCardActivity Result observer
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == NEW_CARD_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK){
            Card card = new Card(data.getStringExtra("cardName"), "image",data.getIntExtra("cycle", 1));
            mApolongoViewModel.insertCard(card);
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
