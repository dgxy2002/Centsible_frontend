package com.example.andyapp.queries;

import android.content.Context;

import com.example.andyapp.queries.mongoModels.LoginModel;
import com.example.andyapp.queries.mongoModels.UserModel;

import retrofit2.Call;
import retrofit2.Callback;

public class UserLoginService {
    private final ApiService api;
    private final Context context;

    public UserLoginService(Context context) {
        this.api = RetrofitClient.getApiService();
        this.context=context;
    }

    private void login(LoginModel login, Callback<UserModel> callback){
        Call<UserModel> call = api.getUser(login);
        call.enqueue(callback);
    }
}
