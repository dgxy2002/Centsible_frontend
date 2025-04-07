package com.example.andyapp.queries;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.andyapp.R;
import com.example.andyapp.models.GetCategoryExpenseModel;
import com.example.andyapp.queries.mongoModels.Expense;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseService {

    private final ApiService apiService;
    private final Context context;
    private final String TAG = "LOGCAT";

    public ExpenseService(Context context) {
        this.apiService = RetrofitClient.getApiService();
        this.context = context;
    }

    public void postExpense(Expense expense) {
        Log.d("API_REQUEST", "Sending Expense: " +
                "Title: " + expense.getTitle() + ", Amount: " + expense.getAmount() + ", UserID: " + expense.getUserID() +
                ", Category: " + expense.getCategory() + ", CreatedDate: " + expense.getCreatedDate());
        apiService.createExpense(expense).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        String responseBody = response.body() != null ? response.body().string() : "No response";
                        Log.d("API_SUCCESS", "Server Response: " + responseBody);
                        Toast.makeText(context, "Success: " + responseBody, Toast.LENGTH_SHORT).show();
                    } else {
                        // Log full error details
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error details";
                        Log.e("API_ERROR", "Response Code: " + response.code() + " - " + errorBody);
                        Toast.makeText(context, "Failed: " + response.code() + " - " + errorBody, Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("API_ERROR", "Error reading response body", e);
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e("NETWORK_ERROR", "Request failed: " + t.getMessage());
                Toast.makeText(context, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    public ArrayList<GetCategoryExpenseModel> fetchTotalExpensesByCategory(String userId) {
        ArrayList<GetCategoryExpenseModel> getCategoryExpenseModels = new ArrayList<>();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                apiService.getTotalExpensesByCategory(userId).enqueue(new Callback<HashMap<String, Double>>() {
                    @Override
                    public void onResponse(Call<HashMap<String, Double>> call, Response<HashMap<String, Double>> response) {

                        if (response.isSuccessful() && response.body() != null) {
                            Log.d(TAG, response.body().toString());
                            HashMap<String, Double> data = response.body();
                            for (String key: data.keySet()){
                                Log.d(TAG, key);
                                double amount = data.get(key);
                                GetCategoryExpenseModel categoryExpenseModel = new GetCategoryExpenseModel(key, amount, R.drawable.dining);
                            }
                        } else {
                            try {
                                Log.d(TAG, response.errorBody().string());
                            } catch(IOException e){
                                Log.e(TAG, "Response code"+ response.code());
                                Log.e(TAG, e.toString());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<HashMap<String, Double>> call, Throwable t) {
                        if (t.getMessage() != null){
                            Log.d(TAG, t.getMessage());
                        }
                    }
                });
            }
        });
        return getCategoryExpenseModels;
    }


}

