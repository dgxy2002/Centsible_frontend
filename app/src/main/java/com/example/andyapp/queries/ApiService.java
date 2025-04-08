package com.example.andyapp.queries;

import com.example.andyapp.models.CategoryAllocation;
import com.example.andyapp.models.NotificationResponse;
import com.example.andyapp.queries.mongoModels.Expense;
import com.example.andyapp.queries.mongoModels.LoginModel;
import com.example.andyapp.queries.mongoModels.UserModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @POST("expenses/post")
    Call<ResponseBody> createExpense(@Body Expense expense);

    @GET("expenses/user/{userId}/total-by-category")
    Call<HashMap<String, Double>> getTotalExpensesByCategory(@Path("userId") String userId);

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

    @GET("users/{userId}/connections")
    Call<ArrayList<Map<String, String>>> getConnections(
            @Path("userId") String userId
    );

    //Get budget by category
    @GET("category-allocations/user/{userId}")
    Call<ArrayList<CategoryAllocation>>getCategoryAllocations(@Path("userId") String userId);

    //Log a new Budget
    @POST("category-allocations")
    Call<ResponseBody>updateBudget(@Body ArrayList<CategoryAllocation> dataModels);

    //Invite a connection
    @POST("users/{inviterUsername}/invite/{inviteeUsername}")
    Call<ResponseBody>inviteConnection(@Path("inviterUsername") String inviterId, @Path("inviteeUsername") String inviteeId);

    //Get Pending Invitations
    @GET("users/{userId}/pending-invitations")
    Call<ArrayList<Map<String, String>>>getInvitations(@Path("userId") String userId);

    //Respond to an Invitation
    @FormUrlEncoded
    @PUT("users/{userId}/respond-invitation/{inviterId}")
    Call<ResponseBody>respondInvitation(@Path("userId") String userId, @Path("inviterId") String inviterId, @Field("accept") boolean accept);

    //Delete a connection
    @DELETE("users/{userId}/connections/{connectionId}")
    Call<ResponseBody>removeConnection(@Path("userId") String userId, @Path("connectionId") String connectionId);
}


