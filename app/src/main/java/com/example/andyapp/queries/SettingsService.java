package com.example.andyapp.queries;

import android.content.Context;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;

import com.example.andyapp.DataSubject;
import com.example.andyapp.models.UserSettings;

import java.io.IOException;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SettingsService {
    private ApiService api;
    private Context context;
    private String TAG = "LOGCAT";

    public SettingsService(Context context) {
        this.api = RetrofitClient.getApiService();
        this.context = context;
    }

    public void getSettings(String username, Handler handler, DataSubject<UserSettings> subject){
        api.getSettings(username).enqueue(new Callback<UserSettings>() {
            @Override
            public void onResponse(Call<UserSettings> call, Response<UserSettings> response) {
                if (response.isSuccessful() && response.body()!= null){
                    UserSettings settings = response.body();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            Log.d(TAG, settings.getFirstname() + settings.getLastname());
                            subject.notifyObservers(settings);
                        }
                    });
                }else{
                    String errorBody = null;
                    try {
                        errorBody = response.errorBody() != null ? response.errorBody().string() : "No error details";
                        Log.e(TAG, "Response Code: " + response.code() + " - " + errorBody);
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<UserSettings> call, Throwable t) {
                if (t.getMessage() != null){
                    Log.e(TAG, t.getMessage());
                }
            }
        });
    }

    public void uploadSettings(String username, Map<String, Object> userSettings){
        api.updateSettings(username, userSettings).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body()!= null){
                    try {
                        String message = response.body().string();
                        Log.d(TAG, message);
                    } catch (IOException e) {
                        Log.d(TAG, e.toString());
                    }
                }else{
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error details";
                        Log.e(TAG, "Response Code: " + response.code() + " - " + errorBody);
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
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
    public void uploadProfilePicture(String username, MultipartBody.Part filePart){
        api.updateProfilePhoto(username, filePart).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body()!= null){
                    try {
                        String url = response.body().string();
                        Log.d(TAG, url);
                    } catch (IOException e) {
                        Log.d(TAG, e.toString());
                    }
                }else{
                    try {
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "No error details";
                        Log.e(TAG, "Response Code: " + response.code() + " - " + errorBody);
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
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
