package com.example.andyapp.queries;

import android.content.Context;
import android.util.Log;

import com.example.andyapp.queries.mongoModels.Budget;

public class budgetService {
    private final ApiService apiService;
    private final Context context;

    public budgetService(ApiService apiService, Context context) {
        this.apiService = apiService;
        this.context = context;
    }

    public void postBudget(Budget budget){
        Log.d("API_REQUEST", String.format("Sending budget:\nuserid: %s\nCategory: %s\nBudget: %.2f\nCreateddate: $s", budget.getUserID(), budget.getCategory(), budget.getBudget(), budget.getCreatedDate()));

    }
}
