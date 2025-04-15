package com.example.andyapp.queries;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.andyapp.DataSubject;
import com.example.andyapp.R;
import com.example.andyapp.models.GroupsModel;
import com.example.andyapp.models.GroupsModels;
import com.example.andyapp.queries.mongoModels.UserModel;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import io.github.muddz.styleabletoast.StyleableToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GroupsService {
    Context context;
    private ApiService api;
    private String TAG = "LOGCAT";
    public GroupsService(Context context) {
        this.context = context;
        this.api = RetrofitClient.getApiService();
    }

    public GroupsModels getConnections(String userId, Handler handler, DataSubject<GroupsModels> subject){
        GroupsModels groupsModels = new GroupsModels(new ArrayList<GroupsModel>());
        api.getConnections(userId).enqueue(new Callback<ArrayList<UserModel>>() {
            @Override
            public void onResponse(Call<ArrayList<UserModel>> call, Response<ArrayList<UserModel>> response) {
                if (response.body() != null){
                    ArrayList<UserModel> connections = response.body();
                    for(UserModel connection: connections){
                        String userId = connection.getId();
                        String username = connection.getUsername();
                        String imageUrl = connection.getImageUrl();
                        Log.d("LOGCAT", "Username: " + username + ", UserId: " + userId);
                        groupsModels.addGroupModel(new GroupsModel(username, userId, imageUrl));
                    }
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            subject.notifyObservers(groupsModels);
                        }
                    });
                }else{
                    try {
                        Log.d(TAG, response.errorBody().string());
                    } catch(IOException e){
                        Log.e(TAG, "Response code"+ response.code());
                        Log.e(TAG, e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<ArrayList<UserModel>> call, Throwable t) {
                if (t.getMessage() != null){
                    Log.d(TAG, t.getMessage());
                }
            }
        });
        return groupsModels;
    }

    public void deleteConnection(String userId, String connectionId){
        api.removeConnection(userId, connectionId).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body()!= null){
                    try {
                        String message = response.body().string();
                        StyleableToast.makeText(context, message, R.style.custom_toast).show();
                        Log.d(TAG, message);
                    } catch (IOException e) {
                        Log.d(TAG, e.toString());
                    }
                }else{
                    try{
                        Log.e(TAG, response.errorBody().string());
                    }catch (IOException e){
                        Log.e(TAG, "Response code" + response.code());
                        Log.e(TAG, e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t.getMessage() != null){
                    Log.d(TAG, t.getMessage());
                }
            }
        });
    }

    public void addConnection(String inviterUsername, String inviteeUsername){
        api.inviteConnection(inviterUsername, inviteeUsername).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body()!= null){
                    try{
                        String message = response.body().string();
                        Log.d(TAG, message);
                        StyleableToast.makeText(context, message, R.style.custom_toast).show();
                    } catch (IOException e) {
                        Log.e(TAG, e.toString());
                    }
                }else{
                    try{
                        Log.e(TAG, response.errorBody().string());
                    }catch (IOException e){
                        Log.e(TAG, "Response code" + response.code());
                        Log.e(TAG, e.toString());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                if (t.getMessage() != null){
                    Log.d(TAG, t.getMessage());
                }
            }
        });
    }
}
