package com.example.andyapp.fragments;

import static androidx.core.content.ContextCompat.getSystemService;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.example.andyapp.LogExpense;
import com.example.andyapp.LoginActivity;
import com.example.andyapp.NavigationDrawerActivity;
import com.example.andyapp.R;
import com.example.andyapp.models.PostIncome;
import com.example.andyapp.queries.ExpenseService;
import com.example.andyapp.queries.IncomeService;
import com.example.andyapp.queries.RetrofitClient;
import com.example.andyapp.queries.mongoModels.PostExpense;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.muddz.styleabletoast.StyleableToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.POST;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link LogExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LogExpenseFragment extends Fragment {
    private String amount = "$";
    private String category = "";
    private String date;

    private ArrayAdapter<String> payAdapter;
    private ArrayAdapter<String> catAdapter;
    private ImageButton btnSubmit;
    private ImageButton btnDelete;
    private ImageButton btnDate;
    private ImageButton btnBack;
    private TextView amountView;
    private EditText descEditText;
    private AutoCompleteTextView dropdownCat;
    private AutoCompleteTextView dropdownType;
    private ExpenseService expenseService;
    private IncomeService incomeService;
    private SharedPreferences myPref;
    private String token;
    private String userId;
    private LocalDate currentDate;
    private String TAG = "LOGCAT";
    private SharedPreferences mPref;

    AppCompatButton[] btnArray = new AppCompatButton[11];
    interface PostExpenseService{
        @POST("expenses/post")
        Call<ResponseBody> postExpenseService(@Body PostExpense expense);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_log_expense, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //SharedPreferences permissions
        mPref = requireContext().getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        userId = mPref.getString(LoginActivity.USERKEY, LoginActivity.DEFAULT_USERID);
        //Initialise Views
        expenseService = new ExpenseService(requireContext());
        incomeService = new IncomeService(requireContext());
        dropdownCat = view.findViewById(R.id.dropdownCategories);
        dropdownType = view.findViewById(R.id.dropdownType);
        amountView = view.findViewById(R.id.amountView);
        btnSubmit = view.findViewById(R.id.btnSubmit);
        btnDelete = view.findViewById(R.id.buttoncross);
        descEditText = view.findViewById(R.id.logExpDesc);
        btnDate = view.findViewById(R.id.btnDate);
        btnArray[0] = view.findViewById(R.id.button0);
        btnArray[1] = view.findViewById(R.id.button1);
        btnArray[2] = view.findViewById(R.id.button2);
        btnArray[3] = view.findViewById(R.id.button3);
        btnArray[4] = view.findViewById(R.id.button4);
        btnArray[5] = view.findViewById(R.id.button5);
        btnArray[6] = view.findViewById(R.id.button6);
        btnArray[7] = view.findViewById(R.id.button7);
        btnArray[8]= view.findViewById(R.id.button8);
        btnArray[9]= view.findViewById(R.id.button9);
        btnArray[10] = view.findViewById(R.id.buttondot);

        //SharedPreferences
        myPref = requireContext().getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        userId = myPref.getString(LoginActivity.USERKEY, LoginActivity.DEFAULT_USERID);

        //Configure dropdown menu
        String[] categories = getResources().getStringArray(R.array.categories);
        String[] transactions = getResources().getStringArray(R.array.transaction_types);
        catAdapter = new ArrayAdapter<String>(requireContext(), R.layout.dropdownitem, categories);
        payAdapter = new ArrayAdapter<String>(requireContext(), R.layout.dropdownitem, transactions);
        dropdownType.setAdapter(payAdapter);
        dropdownCat.setAdapter(catAdapter);
        dropdownCat.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                category = item;
                StyleableToast.makeText(requireContext(), "Item: " + item, R.style.custom_toast).show();
            }
        });
        dropdownType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String item = adapterView.getItemAtPosition(i).toString();
                StyleableToast.makeText(requireContext(), "Item: " + item, R.style.custom_toast).show();
            }
        });
        //Configure EditText
        descEditText.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE ||
                    (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                // Hide Keyboard
                InputMethodManager imm = (InputMethodManager) requireContext().getSystemService(Context.INPUT_METHOD_SERVICE);
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
            btn.setOnClickListener(new LogExpenseFragment.numOnClickListener());
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
                btnSubmit.setEnabled(false);
                String transaction = dropdownType.getText().toString();
                if (transaction != null && transaction.equals("Expense")) {
                    PostExpense expense = getPostExpense();
                    if (expense.getAmount() == 0) {
                        StyleableToast.makeText(requireContext(), "Empty Amount is not allowed!", R.style.custom_toast).show();
                        btnSubmit.setEnabled(true);
                        return;
                    }
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            LogExpenseFragment.PostExpenseService service = RetrofitClient.getRetrofit().create(LogExpenseFragment.PostExpenseService.class);
                            service.postExpenseService(expense).enqueue(new Callback<ResponseBody>() {
                                @Override
                                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                                    if (response.body() != null) {
                                        try {
                                            String message = response.body().string();
                                            StyleableToast.makeText(requireContext(), message, R.style.custom_toast).show();
                                            Intent intent = new Intent(requireContext(), NavigationDrawerActivity.class);
                                            startActivity(intent);
                                            Log.d(TAG, message);
                                        } catch (IOException e) {
                                            Log.d(TAG, e.toString());
                                        }
                                    } else {
                                        try {
                                            btnSubmit.setEnabled(true);
                                            String error = response.errorBody().string();
                                            Log.e(TAG, "Response code: " + response.code());
                                            Log.e(TAG, "Error body: " + error);
                                        } catch (IOException e) {
                                            Log.e(TAG, "Error reading errorBody", e);
                                        }
                                        ;
                                    }
                                }

                                @Override
                                public void onFailure(Call<ResponseBody> call, Throwable t) {
                                    btnSubmit.setEnabled(true);
                                    if (t.getMessage() != null) {
                                        Log.d(TAG, t.getMessage());
                                    }
                                }
                            });
                        }
                    });
                }else if (transaction != null && transaction.equals("Income")){
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    PostIncome data = getPostIncome();
                    executor.execute(new Runnable() {
                        @Override
                        public void run() {
                            incomeService.postIncome(data);
                            Intent intent = new Intent(requireContext(), NavigationDrawerActivity.class);
                            startActivity(intent);
                        }
                    });
                }else{
                    btnSubmit.setEnabled(true);
                    StyleableToast.makeText(requireContext(), "Please enter a transaction type!", R.style.custom_toast).show();
                }
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
        PostExpense expense = new PostExpense(userId, title, amtLogged, category, date);
        Log.d(TAG, expense.toString());
        return expense;
    }

    private PostIncome getPostIncome(){
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
        if (title.isEmpty()){
            title = "No Title";
        }

        PostIncome income = new PostIncome(title, amtLogged, date, userId);
        return income;
    }

    private void openDialog(){
        DatePickerDialog dialog = new DatePickerDialog(requireContext(), new DatePickerDialog.OnDateSetListener() {
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