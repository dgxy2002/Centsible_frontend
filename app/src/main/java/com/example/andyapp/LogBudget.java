package com.example.andyapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.LinearSnapHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SnapHelper;

import com.example.andyapp.managers.ScaleCenterItemLayoutManager;

import com.example.andyapp.adapters.LogBd_RecyclerViewAdapter;
import com.example.andyapp.models.LogBudgetModel;

import java.util.ArrayList;

public class LogBudget extends AppCompatActivity {
    ArrayList<LogBudgetModel> logBudgetModels;
    LogBd_RecyclerViewAdapter recyclerViewAdapter;
    ImageButton btnBack;
    RecyclerView recyclerView;
    LinearSnapHelper snapHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_log_budget);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnBack = findViewById(R.id.logBdBtnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogBudget.this, NavigationDrawerActivity.class);
                startActivity(intent);
            }
        });
        logBudgetModels = new ArrayList<>();
        setUpLogBudgetModels();
        recyclerViewAdapter = new LogBd_RecyclerViewAdapter(LogBudget.this, logBudgetModels);
        recyclerView = findViewById(R.id.logBdRecyclerView);
        recyclerView.setAdapter(recyclerViewAdapter);
        ScaleCenterItemLayoutManager layoutManager = new ScaleCenterItemLayoutManager (this, LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        SnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(recyclerView);
        recyclerView.scrollToPosition(Integer.MAX_VALUE / 2);
    }

    private void setUpLogBudgetModels(){
        String[] categories = getResources().getStringArray(R.array.categories);

        for (int i = 0; i < categories.length; i++){
            String category = categories[i];
            logBudgetModels.add(new LogBudgetModel(category, R.drawable.dining));
        }
    }
}