package com.example.andyapp;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.DatePicker;
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
import com.example.andyapp.queries.mongoModels.PostExpense;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

public class LogExpense extends AppCompatActivity {
    private String amount = "$";
    private String category = "";
    private String date;

    ArrayAdapter<String> payAdapter;
    ArrayAdapter<String> catAdapter;
    ImageButton btnSubmit;
    ImageButton btnDelete;
    ImageButton btnDate;
    ImageButton btnBack;
    TextView amountView;
    EditText descEditText;
    AutoCompleteTextView dropdownCat;
    AutoCompleteTextView dropdownPay;
    ExpenseService expenseService;
    SharedPreferences myPref;
    String token;
    String userId;
    LocalDate currentDate;
    String TAG = "LOGCAT";

    AppCompatButton[] btnArray = new AppCompatButton[11];

    interface PostExpenseService{
        @POST("expenses/post")
        Call<ResponseBody> postExpenseService(@Body PostExpense expense);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_expense);
        //Initialise Views
        expenseService = new ExpenseService(this);
        dropdownCat = findViewById(R.id.dropdownCategories);
        dropdownPay = findViewById(R.id.dropdownPay);
        amountView = findViewById(R.id.amountView);
        btnSubmit = findViewById(R.id.btnSubmit);
        btnDelete = findViewById(R.id.buttoncross);
        descEditText = findViewById(R.id.logExpDesc);
        btnDate = findViewById(R.id.btnDate);
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

        //SharedPreferences
        myPref = getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        userId = myPref.getString(LoginActivity.USERKEY, LoginActivity.DEFAULT_USERID);

        //Configure dropdown menu
        String[] categories = getResources().getStringArray(R.array.categories);
        String[] transactions = getResources().getStringArray(R.array.transaction_types);
        catAdapter = new ArrayAdapter<String>(this, R.layout.dropdownitem, categories);
        payAdapter = new ArrayAdapter<String>(this, R.layout.dropdownitem, transactions);
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
                Toast.makeText(LogExpense.this, "Item: " + item, Toast.LENGTH_SHORT).show();
            }
        });
        //Configure EditText
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
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openDialog();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (amount.length() > 1) {
                    amount = amount.substring(0, amount.length() - 1);
                }
                setAmountViewText();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Enter onSubmit Logic here, category, description, amount, dateCreated
                PostExpense expense = getPostExpense();
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        PostExpenseService service = RetrofitClient.getRetrofit().create(PostExpenseService.class);
                        service.postExpenseService(expense).enqueue(new Callback<ResponseBody>() {
                            @Override
                            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                if (response.body() != null){
                                    try{
                                        String message = response.body().string();
                                        Log.d(TAG, message);
                                    } catch(IOException e){
                                        Log.d(TAG, e.toString());
                                    }
                                }else{
                                    try {
                                        String error = response.errorBody().string();
                                        Log.e(TAG, "Response code: " + response.code());
                                        Log.e(TAG, "Error body: " + error);
                                    } catch (IOException e) {
                                        Log.e(TAG, "Error reading errorBody", e);
                                    };
                                }
                            }
                            @Override
                            public void onFailure(Call<ResponseBody> call, Throwable t) {
                                if (t.getMessage() != null) {
                                    Log.d(TAG, t.getMessage());
                                }
                            }
                        });
                    }
                });
            }
        });

        btnBack = findViewById(R.id.logExpBtnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentBack = new Intent(LogExpense.this, NavigationDrawerActivity.class);
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

    private PostExpense getPostExpense(){
        String title = descEditText.getText().toString();
        double amtLogged;
        if (amount.length() == 1){
            amtLogged = 0;
        }else {
            amtLogged = Double.parseDouble(amount.substring(1));
        }
        if (date != null){
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            currentDate = LocalDate.parse(date, formatter);

        }else{
            currentDate =  LocalDate.now();
            date = "2025-04-04";
        }
        if (category.isEmpty()){
            category = "Others";
        }
        if (title.isEmpty()){
            title = "No Title";
        }

        String userId = "67ecf4e07cb6ed67c0e7e67a"; // Replace with actual user ID
        PostExpense expense = new PostExpense(userId, title, amtLogged, category, date);
        Log.d(TAG, expense.toString());
        return expense;
    }

    private void openDialog(){
        DatePickerDialog dialog = new DatePickerDialog(LogExpense.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                date = String.format("%04d-%02d-%02d", year, month + 1, day);
            }
        },2025, 0, 0);
        dialog.show();
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

