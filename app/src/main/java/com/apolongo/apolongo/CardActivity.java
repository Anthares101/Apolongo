package com.apolongo.apolongo;

import android.app.AlertDialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.apolongo.apolongo.DB.Card;

public class CardActivity extends AppCompatActivity {

    private EditText mEditCardName;
    private EditText mEditBillingCycle;

    private Card mCard;
    private ApolongoViewModel mApolongoViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_card);

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

        mApolongoViewModel = ViewModelProviders.of(this).get(ApolongoViewModel.class);

        Intent intent = getIntent();

        //Initializes a Card object
        int cardId = intent.getIntExtra("CardId", 0);
        String cardName = intent.getStringExtra("CardName");
        int billingCycle = intent.getIntExtra("BillingCycle", 1);

        mCard = new Card(cardName, "image", billingCycle);
        mCard.setCardId(cardId);

        mEditCardName = findViewById(R.id.edit_card);
        mEditCardName.setText(mCard.getCardName());

        mEditBillingCycle = findViewById(R.id.edit_billing_cycle);
        mEditBillingCycle.setText(String.valueOf(mCard.getBillingCycle()));

        final Button buttonSave = findViewById((R.id.button_save));
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();

                if (TextUtils.isEmpty(mEditCardName.getText()) || TextUtils.isEmpty(mEditBillingCycle.getText())){
                    setResult(RESULT_CANCELED, replyIntent);
                }else{
                    String cardName = mEditCardName.getText().toString();
                    String billingCycle = mEditBillingCycle.getText().toString();

                    //No changes detected
                    if(cardName.equals(mCard.getCardName()) &&
                            billingCycle.equals(Integer.toString(mCard.getBillingCycle()))){

                        setResult(RESULT_CANCELED, replyIntent);

                        //It goes back to PurchaseListActivity
                        finish();
                    }
                    else {//Changes detected
                        int number = 0;
                        if(!cardName.equals(mCard.getCardName()))
                            number = mApolongoViewModel.alreadyExist(cardName);

                        if(number == 0) {
                            if (Integer.parseInt(billingCycle) <= 0 || Integer.parseInt(billingCycle) > 31) {
                                setResult(RESULT_CANCELED, replyIntent);
                                Toast.makeText(getApplicationContext(), R.string.cycle_not_valid, Toast.LENGTH_LONG).show();
                            } else {//All correct
                                replyIntent.putExtra("CardId", mCard.getCardId());
                                replyIntent.putExtra("CardName", cardName);
                                replyIntent.putExtra("BillingCycle", billingCycle);
                                setResult(RESULT_OK, replyIntent);

                                //It goes back to PurchaseListActivity
                                finish();
                            }
                        }
                        else {//Already exists a card with the same name introduced
                            setResult(RESULT_CANCELED, replyIntent);
                            Toast.makeText(getApplicationContext(), R.string.alreadyExist_not_saved, Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });

        final Button buttonDelete = findViewById((R.id.button_delete));
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Borrar tarjeta "+mCard.getCardName());
                builder.setMessage("Eliminará todas las compras relacionadas");

                builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent replyIntent = new Intent();

                        replyIntent.putExtra("CardId", mCard.getCardId());
                        replyIntent.putExtra("CardName", mCard.getCardName());
                        replyIntent.putExtra("BillingCycle", mCard.getBillingCycle());
                        setResult(MainActivity.RESULT_DELETE, replyIntent);

                        finish();
                    }
                });
                builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
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
