package com.example.andyapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.andyapp.queries.ApiService;
import com.example.andyapp.queries.ExpenseService;
import com.example.andyapp.queries.RetrofitClient;
import com.example.andyapp.queries.mongoModels.Expense;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogExpense extends AppCompatActivity {
    private String amount = "$";
    private String category = "";
    private String payOption = "";

    ArrayAdapter<String> payAdapter;
    ArrayAdapter<String> catAdapter;
    ImageButton btnSubmit;
    ImageButton btnDelete;
    Button btnBack;
    TextView amountView;
    EditText descEditText;
    AutoCompleteTextView dropdownCat;
    AutoCompleteTextView dropdownPay;
    ExpenseService expenseService;

    AppCompatButton[] btnArray = new AppCompatButton[11];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_expense);
        //Initialise Views
        dropdownCat = findViewById(R.id.dropdownCategories);
        dropdownPay = findViewById(R.id.dropdownPay);
        descEditText = findViewById(R.id.descEditText);
        amountView = findViewById(R.id.amountView);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnDelete = findViewById(R.id.buttoncross);
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

        //Configure dropdown menu
        String[] categories = getResources().getStringArray(R.array.categories);
        String[] payOptions = getResources().getStringArray(R.array.payOptions);
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

        descEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {

                // Hide Keyboard
                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }

                // Clear focus from EditText
                descEditText.clearFocus();
                return true;
            }
            return false;
        });

        //Configure button on click listeners
        for (Button btn: btnArray){
            btn.setOnClickListener(new numOnClickListener());
        }
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount.length() > 1) {
                    amount = amount.substring(0, amount.length() - 1);
                }
                setAmountViewText();
            }
        });
        ImageButton btnSubmit = findViewById(R.id.btnSubmit);
        expenseService = new ExpenseService(this);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enter onSubmit Logic here, category, description, amount, dateCreated
                String title = descEditText.getText().toString();
                float amtLogged;
                if (amount.length() == 1){
                    amtLogged = 0;
                }else {
                    amtLogged = Float.parseFloat(amount.substring(1));
                }
                String userId = "67d3cbd26b238d2f9f63855b"; // Replace with actual user ID
                String currentDate = "2025-03-18";
                expenseService.postExpense(title, amtLogged, userId, category, currentDate);
            }
        });

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                startActivity(intentBack);
            }
        });
    }

    public void setAmountViewText(){
        amountView.setText(amount);
        if (amount.length() > 4) {
            amountView.setTextSize((float) 500.0 / (amount.length() + 3));
        }
        amountView.getLayoutParams().width = 200 * amount.length();
        amountView.requestLayout();
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
            //Text Formatting
            setAmountViewText();
        }
    }
}

//TODO Create onSubmit Logic: Need to parse amount into a BigDecimal and then check for null strings
//TODO In category and payOptions
//TODO: If have time, please place files properly
//TODO Add button icons