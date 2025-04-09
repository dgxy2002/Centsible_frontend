package com.example.andyapp.queries;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.andyapp.DataSubject;
import com.example.andyapp.LogBudget;
import com.example.andyapp.R;
import com.example.andyapp.models.BudgetModel;
import com.example.andyapp.models.BudgetModels;
import com.example.andyapp.models.CategoryAllocation;
import com.example.andyapp.models.LogBudgetModel;
import com.example.andyapp.models.LogBudgetModels;
import com.example.andyapp.models.PostCategoryAllocation;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BudgetService {
    ApiService api;
    Context context;
    private final String TAG = "LOGCAT";

    public BudgetService(Context context) {
        this.context = context;
        this.api = RetrofitClient.getApiService();
    }

    public void getBudgetCategories(String userId, Handler handler, DataSubject<BudgetModels> subject){
        BudgetModels budgetModels = new BudgetModels();
        api.getCategoryAllocations(userId).enqueue(new Callback<ArrayList<CategoryAllocation>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryAllocation>> call, Response<ArrayList<CategoryAllocation>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    ArrayList<CategoryAllocation> data = response.body();
                    for(CategoryAllocation model: data){
                        String category = model.getCategory();
                        double budget = model.getAllocatedAmount();
                        if (budget != 0){
                            double spent = model.getSpentAmount(); //replace later
                            Log.d(TAG, category);
                            Log.d(TAG, String.valueOf(budget));
                            budgetModels.addBudgetModel(new BudgetModel(R.drawable.dining, spent, budget, category));
                        }
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            subject.notifyObservers(budgetModels);
                        }
                    });
                }else{
                    try{
                        Log.e(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                        Log.e(TAG, "Response Code: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CategoryAllocation>> call, Throwable t) {
                if (t.getMessage() != null){
                    Log.d(TAG, t.getMessage());
                    Log.d(TAG, "BudgetService failed");
                }
            }
        });
    }

    public LogBudgetModels getLogBudgetCategories(String userId, Handler handler, DataSubject<LogBudgetModels> subject){
        LogBudgetModels logBudgetModels = new LogBudgetModels();
        ArrayList<String> categories = new ArrayList<>(Arrays.asList(context.getResources().getStringArray(R.array.categories)));
        api.getCategoryAllocations(userId).enqueue(new Callback<ArrayList<CategoryAllocation>>() {
            @Override
            public void onResponse(Call<ArrayList<CategoryAllocation>> call, Response<ArrayList<CategoryAllocation>> response) {
                if (response.isSuccessful() && response.body()!=null){
                    ArrayList<CategoryAllocation> data = response.body();
                    for (int i = 0; i < categories.size(); i++){
                        logBudgetModels.addLogBudgetModel(new LogBudgetModel(i, categories.get(i), R.drawable.othercategory, "0"));
                    }
                    for(CategoryAllocation model: data){
                        String category = model.getCategory();
                        double budget = model.getAllocatedAmount();
                        int id = categories.indexOf(category);
                        Log.d(TAG, category);
                        Log.d(TAG, String.valueOf(budget));
                        logBudgetModels.updateBudgetModel(id, String.valueOf(budget));
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            subject.notifyObservers(logBudgetModels);
                        }
                    });
                }else{
                    try{
                        Log.e(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                        Log.e(TAG, "Response Code: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<CategoryAllocation>> call, Throwable t) {
                if (t.getMessage() != null){
                    Log.d(TAG, t.getMessage());
                    Log.d(TAG, "BudgetService failed");
                }
            }
        });
        return logBudgetModels;
    }

    public void updateBudgetData(ArrayList<PostCategoryAllocation> dataModels){
        api.updateBudget(dataModels).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body()!=null){
                    try {
                        String message = response.body().string();
                        Log.d(TAG, message);
                        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }

                }else{
                    try{
                        Log.e(TAG, response.errorBody().string());
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                        Log.e(TAG, "Response Code: " + response.code());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t.getMessage() != null){
                    Log.d(TAG, t.getMessage());
                }
            }
        });
    }



}
