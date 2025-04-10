package com.example.andyapp.queries;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.andyapp.DataSubject;
import com.example.andyapp.models.FetchIncome;
import com.example.andyapp.models.PostIncome;
import com.example.andyapp.utils.SortIncomeByDay;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class IncomeService {
    ApiService api;
    Context context;

    private String TAG = "LOGCAT";

    public IncomeService(Context context) {
        this.api = RetrofitClient.getApiService();
        this.context = context;
    }

    public FetchIncomes fetchIncomesByMonth(String userId, int month, int year, DataSubject<FetchIncomes> subject, Handler handler){
        FetchIncomes fetchIncomes = new FetchIncomes(new ArrayList<>());
        api.getIncomeForMonth(userId, year, month).enqueue(new Callback<ArrayList<FetchIncome>>() {
            @Override
            public void onResponse(Call<ArrayList<FetchIncome>> call, Response<ArrayList<FetchIncome>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ArrayList<FetchIncome> incomes = response.body();
                    fetchIncomes.setFetchIncomeArrayList(incomes);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            subject.notifyObservers(fetchIncomes);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<ArrayList<FetchIncome>> call, Throwable t) {
                if (t.getMessage() != null){
                    Log.e(TAG, t.getMessage());
                }
            }
        });
        return fetchIncomes;
    }

    public void postIncome(PostIncome data){
        api.postIncome(data).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null){
                    String message = null;
                    try {
                        message = response.body().string();
                        Log.d(TAG, message);
                    } catch (IOException e) {
                        Log.d(TAG, e.toString());
                    }
                } else{
                    try {
                        Log.d(TAG, response.errorBody().string());
                        Log.d(TAG, "response code" + response.code());
                    } catch (IOException e) {
                        Log.d(TAG, e.toString());
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t.getMessage() != null){
                    Log.e(TAG, t.getMessage());
                }
            }
        });
    }
}
