package com.example.andyapp.queries;

import com.example.andyapp.models.NotificationResponse;
import com.example.andyapp.queries.mongoModels.Expense;
import com.example.andyapp.queries.mongoModels.LoginModel;
import com.example.andyapp.queries.mongoModels.UserModel;

import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {
    @POST("expenses/post")
    Call<ResponseBody> createExpense(@Body Expense expense);

    @GET("expenses/user/{userId}/total-by-category")
    Call<Map<String, Double>> getTotalExpensesByCategory(@Path("userId") String userId);

    @GET("/users/login")
    Call<UserModel> getUser(@Body LoginModel login);

    @GET("expenses/user/{userId}")
    Call<List<Expense>> getUserExpenses(@Header("Authorization") String token,@Path("userId") String userId);

    @GET("users/{userId}/notifications")
    Call<List<NotificationResponse>> getNotifications(
            @Header("Authorization") String token,
            @Path("userId") String userId
    );

    @GET("users/{userId}/notifications/unread-count")
    Call<Integer> getUnreadCount(
            @Header("Authorization") String token,
            @Path("userId") String userId
    );
}


