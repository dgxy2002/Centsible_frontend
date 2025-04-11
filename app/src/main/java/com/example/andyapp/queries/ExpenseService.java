package com.example.andyapp.queries;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.andyapp.DataObserver;
import com.example.andyapp.DataSubject;
import com.example.andyapp.R;
import com.example.andyapp.models.GetCategoryExpenseModel;
import com.example.andyapp.models.GetCategoryExpenseModels;
import com.example.andyapp.queries.mongoModels.Expense;
import com.example.andyapp.utils.GetIcons;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

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
                ", Category: " + expense.getCategory() + ", CreatedDate: " + expense.getParsedCreatedDate());
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

    public GetCategoryExpenseModels fetchTotalExpensesByCategory(String userId, Handler handler, DataSubject<GetCategoryExpenseModels> subject) {
        GetCategoryExpenseModels categoryExpenseModels = new GetCategoryExpenseModels(new ArrayList<GetCategoryExpenseModel>());
        apiService.getTotalExpensesByCategory(userId).enqueue(new Callback<HashMap<String, Double>>() {
            @Override
            public void onResponse(Call<HashMap<String, Double>> call, Response<HashMap<String, Double>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, response.body().toString());
                    HashMap<String, Double> data = response.body();
                    for (String key: data.keySet()){
                        Log.d(TAG, key);
                        double amount = data.get(key);
                        int image = new GetIcons().getIcon(key);
                        GetCategoryExpenseModel categoryExpenseModel = new GetCategoryExpenseModel(key, amount, image);
                        ArrayList<GetCategoryExpenseModel> model =  categoryExpenseModels.getCategoryExpensesModels();
                        model.add(categoryExpenseModel);
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            subject.notifyObservers(categoryExpenseModels);
                        }
                    });
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
        return categoryExpenseModels;
    }
}

