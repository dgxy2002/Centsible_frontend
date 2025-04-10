package com.example.andyapp;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.andyapp.models.LogBudgetModel;

import java.util.ArrayList;

public class LogBudgetCategoryActivity extends AppCompatActivity {
    ImageButton btnBack;
    ImageButton btnIncrease;
    ImageButton btnDecrease;
    EditText budgetEditText;
    TextView categoryTextView;
    Intent intent;
    String categoryText;
    String budgetAmount;
    int id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_budget_category);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        intent = getIntent();
        if(intent!=null){
            categoryText = intent.getStringExtra(LogBudget.CAT_KEY);
            budgetAmount = intent.getStringExtra(LogBudget.BD_KEY);
            id = intent.getIntExtra(LogBudget.ID_KEY, 0);
//            logBudgetModels = intent.getParcelableArrayListExtra(LogBudget.MODEL_KEY);
        }else{
            categoryText = "Category";
            budgetAmount = "0.00";
            id = 0;
        }
        btnBack = findViewById(R.id.logBdCategoryBtnBack);
        btnIncrease = findViewById(R.id.logBdBtnIncrease);
        btnDecrease = findViewById(R.id.LogBdBtnDecrease);
        budgetEditText = findViewById(R.id.editTextNumberDecimal);
        categoryTextView = findViewById(R.id.logBudgetCategoryNameTV);
        categoryTextView.setText(String.format("For: %s", categoryText));
        budgetEditText.setText(budgetAmount);

        btnIncrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                budgetAmount = budgetEditText.getText().toString().substring(1);
                double amount = Double.parseDouble(budgetAmount);
                amount += 1;
                budgetEditText.setText(String.valueOf(amount));
            }
        });

        btnDecrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                budgetAmount = budgetEditText.getText().toString().substring(1);
                double amount = Double.parseDouble(budgetAmount);
                amount -= 1;
                budgetEditText.setText(String.valueOf(amount));
            }
        });


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                budgetAmount = budgetEditText.getText().toString().substring(1);
                if (budgetAmount.isEmpty()){
                    budgetAmount = "0.00";
                }
                intent.putExtra(LogBudget.BD_KEY, budgetAmount);
                intent.putExtra(LogBudget.ID_KEY, id);
                setResult(LogBudget.RESULT_OK,intent);
                finish();
            }
        });

        budgetEditText.addTextChangedListener(new TextWatcher() {
            boolean isEditing = false;

            @Override
            public void afterTextChanged(Editable s) {
                if (isEditing) return;
                isEditing = true;
                String input = s.toString();
                if (input.contains(".")) {
                    int index = input.indexOf(".");
                    if (index + 3 < input.length()) {
                        input = input.substring(0, index + 3); // limit to 2dp
                    }
                }
                if (input.startsWith(".")) {
                    input = "0" + input; //Edge case whr if you start with a .
                }
                input = input.replace("$", "");
                budgetEditText.setText(String.format("$%s",input));
                budgetEditText.setSelection(budgetEditText.getText().length());
                isEditing = false;
            }

            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
        });


    }
}