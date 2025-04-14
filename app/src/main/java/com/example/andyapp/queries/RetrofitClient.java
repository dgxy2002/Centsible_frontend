package com.example.andyapp.queries;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String BASE_URL = "https://five0-001.onrender.com/api/";
    private static Retrofit retrofit;
    private static final ApiService api = getRetrofit().create(ApiService.class);

    public static ApiService getApiService() {
        return api;
    }
    public static Retrofit getRetrofit() {
        if (retrofit == null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
