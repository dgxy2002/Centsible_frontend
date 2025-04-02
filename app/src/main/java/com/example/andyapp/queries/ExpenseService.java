package com.example.andyapp.queries;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.andyapp.queries.mongoModels.Expense;

import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ExpenseService {

    private final ApiService apiService;
    private final Context context;

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

    public void fetchTotalExpensesByCategory(String userId, ExpenseCallback callback) {
        apiService.getTotalExpensesByCategory(userId).enqueue(new Callback<Map<String, Double>>() {
            @Override
            public void onResponse(Call<Map<String, Double>> call, Response<Map<String, Double>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    callback.onSuccess(response.body()); // Pass data back
                } else {
                    callback.onError("Failed to fetch expenses: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Map<String, Double>> call, Throwable t) {
                callback.onError("Network error: " + t.getMessage());
            }
        });
    }

    public interface ExpenseCallback {
        void onSuccess(Map<String, Double> categoryExpenses);
        void onError(String errorMessage);
    }
}

