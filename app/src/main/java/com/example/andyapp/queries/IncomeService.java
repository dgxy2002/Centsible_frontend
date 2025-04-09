package com.example.andyapp.queries;

import android.content.Context;
import android.os.Handler;
import android.util.Log;

import com.example.andyapp.DataSubject;
import com.example.andyapp.models.FetchIncome;

import java.lang.reflect.Array;
import java.util.ArrayList;

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

    public void fetchIncomesByMonth(String userId, String month, String year, DataSubject<FetchIncomes> subject, Handler handler){
        FetchIncomes fetchIncomes = new FetchIncomes(new ArrayList<>());
        api.getIncomeForMonth(userId, month).enqueue(new Callback<ArrayList<FetchIncome>>() {
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
    }
}
