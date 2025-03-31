package com.example.andyapp.queries;

import com.example.andyapp.queries.mongoModels.Expense;

import java.util.Map;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("expenses/post")
    Call<ResponseBody> createExpense(@Body Expense expense);

    @GET("expenses/user/{userId}/total-by-category")
    Call<Map<String, Double>> getTotalExpensesByCategory(@Path("userId") String userId);

    @GET("expenses/user/{userId}")
    Call<List<Expense>> getUserExpenses(@Path("userId") String userId);




}


