package com.apolongo.apolongo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class NewPurchaseActivity extends AppCompatActivity {

    public static final String EXTRA_REPLY = "com.apolongo.android.purchaselistsql.REPLY";

    private EditText mEditPurchaseName;
    private EditText mEditPurchasePrice;
    private EditText mEditPurchaseDate;


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_purchase);
        mEditPurchaseName = findViewById(R.id.edit_purchase_name);
        mEditPurchasePrice = findViewById(R.id.edit_purchase_price);
        mEditPurchaseDate = findViewById(R.id.edit_purchase_date);


        final Button button = findViewById((R.id.button_save));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent replyIntent = new Intent();
                if (TextUtils.isEmpty(mEditPurchaseName.getText())){
                    setResult(RESULT_CANCELED, replyIntent);
                }else{
                    String purchaseName = mEditPurchaseName.getText().toString();
                    String purchaseDate = mEditPurchaseDate.getText().toString();
                    float purchasePrice = Float.parseFloat(mEditPurchaseDate.getText().toString());
                    replyIntent.putExtra("name", purchaseName);
                    replyIntent.putExtra("date", purchaseDate);
                    replyIntent.putExtra("price", purchasePrice);
                    setResult(RESULT_OK, replyIntent);
                    //It goes back to MainActivity
                    finish();
                }
            }
        });

    }
}
