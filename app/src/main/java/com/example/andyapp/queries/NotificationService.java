package com.example.andyapp.queries;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.ImageButton;

import com.example.andyapp.DataObserver;
import com.example.andyapp.DataSubject;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationService {
    private ApiService api;
    private Context context;
    private static String TAG = "LOGCAT";
    public NotificationService(Context context) {
        this.api = RetrofitClient.getApiService();
        this.context = context;
    }
    public void fetchUnreadNotificationCount(String token, String userId, Handler handler, DataObserver<Integer> btnBarRightObserver) {
        api.getUnreadCount(token, userId).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int count = response.body();
                    Log.d(TAG, String.valueOf(count));
                    if (count > 0){
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                btnBarRightObserver.updateData(count);
                            }
                        });
                    }
                }
            }
            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                if(t.getMessage() != null){
                    Log.d(TAG, t.getMessage());
                }
            }
        });
    }

    public void sendNudge(String fromUsername, String toUsername, Handler handler){
        api.sendNudge(fromUsername, toUsername).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null){
                    try {
                        String message = response.body().string();
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                Log.d(TAG, message);
                            }
                        });
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                } else{
                    if(response.errorBody() != null){
                        Log.d(TAG, response.errorBody().toString());
                        Log.d(TAG, "Response Code: " + response.code());
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
