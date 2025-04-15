package com.example.andyapp.queries;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.andyapp.DataObserver;
import com.example.andyapp.queries.mongoModels.UserModel;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserService {
    private ApiService api;
    private Context context;
    private String TAG = "LOGCAT";

    public UserService(Context context) {
        this.api = RetrofitClient.getApiService();
        this.context = context;
    }
    public void getUserImage(String id, Handler handler, ImageView profilePicView){
        api.getUserObject(id).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null){
                    String imageUrl = response.body().getImageUrl();
                    handler.post(new Runnable(){
                        @Override
                        public void run() {
                            Log.d(TAG, imageUrl);
                            Glide.with(context).load(imageUrl).circleCrop().into(profilePicView);
                            profilePicView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (t.getMessage() != null){
                    Log.e(TAG, t.getMessage());
                }
            }
        });
    }

    public void getUserQuest(String id, Handler handler, DataObserver<Boolean> logQuestObserver, DataObserver<Boolean> nudgeQuestObserver, DataObserver<Boolean> viewQuestObserver){
        api.getUserObject(id).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null){
                    UserModel user = response.body();
                    String getLastLog = user.getLastLog();
                    String getLastNudge = user.getLastNudge();
                    String getLastCheckChild = user.getLastCheckChild();
                    Log.d(TAG, getLastLog + getLastNudge);
                    handler.post(new Runnable(){
                        @Override
                        public void run() {
                            nudgeQuestObserver.updateData(LocalDate.parse(getLastNudge).equals(LocalDate.now()));
                            logQuestObserver.updateData(LocalDate.parse(getLastLog).equals(LocalDate.now()));
                            viewQuestObserver.updateData(LocalDate.parse(getLastCheckChild).equals(LocalDate.now()));
                        }
                    });
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                if (t.getMessage() != null){
                    Log.e(TAG, t.getMessage());
                }
            }
        });
    }

    public void updateViewDashboard(String username){
        api.updateViewConnection(username).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null){
                    try {
                        String message = response.body().string();
                        Log.d(TAG, message);
                    } catch (IOException e) {
                        Log.d(TAG, e.toString());
                    }
                }else{
                    try {
                        String error = response.errorBody().string();
                        Log.d(TAG, error);
                        Log.d(TAG, "Response Code: " + response.code());
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t.getMessage()!=null) {
                    Log.d(TAG, t.getMessage());
                }
            }
        });
    }
}
