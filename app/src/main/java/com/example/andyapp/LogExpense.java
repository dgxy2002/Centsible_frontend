package com.example.andyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.textfield.TextInputLayout;

public class LogExpense extends AppCompatActivity {
    AutoCompleteTextView autoCompleteTextView;
    private String amount = "$";
    private String category = "";
    private String payOption = "";

    ArrayAdapter<String> payAdapter;
    ArrayAdapter<String> catAdapter;
    Button[] btnArray = new Button[12];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_expense);

        String[] categories = getResources().getStringArray(R.array.categories);
        String[] payOptions = getResources().getStringArray(R.array.payOptions);

        AutoCompleteTextView dropdownCat = findViewById(R.id.dropdownCategories);
        AutoCompleteTextView dropdownPay = findViewById(R.id.dropdownPay);
        catAdapter = new ArrayAdapter<String>(this, R.layout.dropdownitem, categories);
        payAdapter = new ArrayAdapter<String>(this, R.layout.dropdownitem, payOptions);

        dropdownPay.setAdapter(payAdapter);
        dropdownCat.setAdapter(catAdapter);

        dropdownCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                category = item;
                Toast.makeText(LogExpense.this, "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        dropdownPay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                payOption = item;
                Toast.makeText(LogExpense.this, "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });

        btnArray[0] = findViewById(R.id.button0);
        btnArray[1] = findViewById(R.id.button1);
        btnArray[2] = findViewById(R.id.button2);
        btnArray[3] = findViewById(R.id.button3);
        btnArray[4] = findViewById(R.id.button4);
        btnArray[5] = findViewById(R.id.button5);
        btnArray[6] = findViewById(R.id.button6);
        btnArray[7] = findViewById(R.id.button7);
        btnArray[8]= findViewById(R.id.button8);
        btnArray[9]= findViewById(R.id.button9);
        btnArray[10] = findViewById(R.id.buttondot);
        btnArray[11] = findViewById(R.id.buttoncross);

        for (Button btn: btnArray){
            btn.setOnClickListener(new numOnClickListener());
        }

        Button btnSubmit = findViewById(R.id.buttonsubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enter onSubmit Logic here

            }
        });

        Button btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                startActivity(intentBack);
            }
        });
    }


    class numOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            int id = view.getId();

            if (id == R.id.buttoncross){
                if (amount.length() > 1) {
                    amount = amount.substring(0, amount.length() - 1);
                }
            }
            else {
                if (amount.length() < 4 || amount.charAt(amount.length() - 3) != 46) {
                    if (id == R.id.button0) {
                        amount += "0";
                    } else if (id == R.id.button1) {
                        amount += "1";
                    } else if (id == R.id.button2) {
                        amount += "2";
                    } else if (id == R.id.button3) {
                        amount += "3";
                    } else if (id == R.id.button4) {
                        amount += "4";
                    } else if (id == R.id.button5) {
                        amount += "5";
                    } else if (id == R.id.button6) {
                        amount += "6";
                    } else if (id == R.id.button7) {
                        amount += "7";
                    } else if (id == R.id.button8) {
                        amount += "8";
                    } else if (id == R.id.button9) {
                        amount += "9";
                    } else if (id == R.id.buttondot) {
                        if (amount.length() == 1){
                            amount += "0";
                        }
                        amount += ".";
                    }
                }
            }
            TextView amountView = findViewById(R.id.amountView);
            //Text Formatting
            amountView.setText(amount);
            if (amount.length() > 4) {
                amountView.setTextSize((float) 500.0 / (amount.length() + 3));
            }
            amountView.getLayoutParams().width = 200 * amount.length();
            amountView.requestLayout();
        }
    }
}

//TODO Create onSubmit Logic: Need to parse amount into a BigDecimal and then check for null strings
//TODO In category and payOptions
//TODO: If have time, please place files properly
//TODO Add button icons