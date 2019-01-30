package com.apolongo.apolongo;

import android.app.DatePickerDialog;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
        import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
        import android.widget.TextSwitcher;
        import android.widget.TextView;

import com.apolongo.apolongo.DB.Card;
import com.apolongo.apolongo.DB.Purchase;
import com.apolongo.apolongo.Fragments.DatePickerFragment;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class PurchaseActivity extends AppCompatActivity {

    private Purchase mPurchase;
    private EditText mEditPurchaseName;
    private EditText mEditPurchasePrice;
    private EditText mEditPurchaseDate;
    private EditText mEditPurchaseDesc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_purchase);

        //Initializes a Purchase object
        Intent intent = getIntent();
        
        int purchaseId = intent.getIntExtra("PurchaseId", 0);
        String purchaseName = intent.getStringExtra("PurchaseName");
        float purchasePrice = intent.getFloatExtra("PurchasePrice", 0);
        Date purchaseDate = (Date)intent.getSerializableExtra("PurchaseDate");
        String purchaseDesc = intent.getStringExtra("PurchaseDesc");
        String purchaseCardName = intent.getStringExtra("PurchaseCardName");

        mPurchase = new Purchase(purchaseName, purchaseDate, purchasePrice, purchaseDesc, purchaseCardName);
        mPurchase.setPurchaseId(purchaseId);

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

        final Button button = findViewById((R.id.button_save));
        button.setOnClickListener(new View.OnClickListener() {
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


    }
}
