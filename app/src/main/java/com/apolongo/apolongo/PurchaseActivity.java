package com.apolongo.apolongo;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.apolongo.apolongo.DB.Purchase;
import com.apolongo.apolongo.Fragments.DatePickerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PurchaseActivity extends AppCompatActivity {

    private EditText mEditPurchaseName;
    private EditText mEditPurchasePrice;
    private EditText mEditPurchaseDate;
    private EditText mEditPurchaseDesc;

    private Purchase mPurchase;
    //Cycle dates are used for the return action
    private Date mStartDate;
    private Date mFinishDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase);

        //Initializes the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Intent intent = getIntent();
        mStartDate  = (Date)intent.getSerializableExtra("StartDate");
        mFinishDate  = (Date)intent.getSerializableExtra("FinishDate");

        //Initializes a Purchase object
        int purchaseId = intent.getIntExtra("PurchaseId", 0);
        String purchaseName = intent.getStringExtra("PurchaseName");
        float purchasePrice = intent.getFloatExtra("PurchasePrice", 0);
        Date purchaseDate = (Date)intent.getSerializableExtra("PurchaseDate");
        String purchaseDesc = intent.getStringExtra("PurchaseDesc");
        String purchaseCardName = intent.getStringExtra("PurchaseCardName");

        mPurchase = new Purchase(purchaseName, purchaseDate, purchasePrice, purchaseDesc, purchaseCardName);
        mPurchase.setPurchaseId(purchaseId);

        //Add a back button
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), PurchaseListActivity.class);

                intent.putExtra("startDate", mStartDate);
                intent.putExtra("finishDate", mFinishDate);
                intent.putExtra("cardName", mPurchase.getPurchaseCardName());

                startActivity(intent);
            }
        });

        DateFormat format = new SimpleDateFormat("dd / MM / yyyy", Locale.ENGLISH);

        mEditPurchaseName = findViewById(R.id.edit_purchase_name);
        mEditPurchaseName.setText(mPurchase.getPurchaseName());

        mEditPurchasePrice = findViewById(R.id.edit_purchase_price);
        mEditPurchasePrice.setText(String.valueOf(mPurchase.getPurchasePrice()));

        mEditPurchaseDate = findViewById(R.id.edit_purchase_date);
        mEditPurchaseDate.setText(format.format(mPurchase.getPurchaseDate()));

        mEditPurchaseDesc = findViewById(R.id.edit_purchase_desc);
        mEditPurchaseDesc.setText(mPurchase.getPurchaseSDescp());

        mEditPurchaseDate.setOnClickListener(new View.OnClickListener() {
            private void showDatePickerDialog() {
                DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        // +1 because january is zero
                        final String selectedDate = day + " / " + (month+1) + " / " + year;
                        mEditPurchaseDate.setText(selectedDate);
                    }
                });
                newFragment.show(getSupportFragmentManager(), "datePicker");
            }

            @Override
            public void onClick(View view){
                switch (view.getId()){
                    case R.id.edit_purchase_date:
                        showDatePickerDialog();
                        break;
                }
            }
        });

        final Button buttonSave = findViewById((R.id.button_save));
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent replyIntent = new Intent();
                DateFormat format = new SimpleDateFormat("dd / MM / yyyy", Locale.ENGLISH);

                if (TextUtils.isEmpty(mEditPurchaseName.getText()) || TextUtils.isEmpty(mEditPurchaseDate.getText()) || TextUtils.isEmpty(mEditPurchasePrice.getText())){
                    setResult(RESULT_CANCELED, replyIntent);
                }else{
                    String purchaseName = mEditPurchaseName.getText().toString();
                    String purchaseDate = mEditPurchaseDate.getText().toString();
                    String purchasePrice = mEditPurchasePrice.getText().toString();
                    String purchaseDesc = mEditPurchaseDesc.getText().toString();

                    //No changes detected
                    if(purchaseName.equals(mPurchase.getPurchaseName()) &&
                            purchaseDate.equals(format.format(mPurchase.getPurchaseDate())) &&
                            purchasePrice.equals(Float.toString(mPurchase.getPurchasePrice())) &&
                            purchaseDesc.equals(mPurchase.getPurchaseSDescp())){

                        setResult(RESULT_CANCELED, replyIntent);
                    }
                    else {//Changes detected
                        replyIntent.putExtra("purchaseId", mPurchase.getPurchaseId());
                        replyIntent.putExtra("purchaseName", purchaseName);
                        replyIntent.putExtra("purchaseDate", purchaseDate);
                        replyIntent.putExtra("purchasePrice", purchasePrice);
                        replyIntent.putExtra("purchaseDesc", purchaseDesc);
                        setResult(RESULT_OK, replyIntent);
                    }
                    //It goes back to PurchaseListActivity
                    finish();
                }
            }
        });

        final Button buttonDelete = findViewById((R.id.button_delete));
        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(view.getContext());
                builder.setTitle("Borrar compra "+mPurchase.getPurchaseName());
                builder.setMessage("Eliminará definitivamente esta compra");

                builder.setPositiveButton("Borrar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent replyIntent = new Intent();

                        replyIntent.putExtra("purchaseId", mPurchase.getPurchaseId());
                        replyIntent.putExtra("purchaseName", mPurchase.getPurchaseName());
                        replyIntent.putExtra("purchaseDate", mPurchase.getPurchaseDate());
                        replyIntent.putExtra("purchasePrice", mPurchase.getPurchasePrice());
                        replyIntent.putExtra("purchaseDesc", mPurchase.getPurchaseSDescp());
                        setResult(PurchaseListActivity.RESULT_DELETE, replyIntent);

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
