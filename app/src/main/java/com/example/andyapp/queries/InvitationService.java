package com.example.andyapp.queries;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.andyapp.DataSubject;
import com.example.andyapp.R;
import com.example.andyapp.models.InvitationModel;
import com.example.andyapp.models.InvitationModels;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import io.github.muddz.styleabletoast.StyleableToast;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class InvitationService {
    Context context;
    ApiService api;
    private String TAG = "LOGCAT";

    public InvitationService(Context context) {
        this.context = context;
        this.api = RetrofitClient.getApiService();
    }

    public InvitationModels getInvites(String userId, Handler handler, DataSubject<InvitationModels> subject){
        InvitationModels invitationModels = new InvitationModels();
        ArrayList<InvitationModel> models = invitationModels.getInvitationModels();
        api.getInvitations(userId).enqueue(new Callback<ArrayList<Map<String, String>>>() {
            @Override
            public void onResponse(Call<ArrayList<Map<String, String>>> call, Response<ArrayList<Map<String, String>>> response) {
                if (response.isSuccessful() && response.body() != null){
                    ArrayList<Map<String, String>> invites = response.body();
                    for(Map<String, String> invite: invites){
                        for (Map.Entry<String, String> entry : invite.entrySet()) {
                            String inviterId = entry.getKey();
                            String inviterUsername = entry.getValue();
                            Log.d("LOGCAT", "Inviter Username: " + inviterUsername + ", Invite UserId: " + inviterId);
                            models.add(new InvitationModel(R.drawable.avatar, inviterUsername, inviterId));
                        }
                    }
                    subject.notifyObservers(invitationModels);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Map<String, String>>> call, Throwable t) {
                if (t.getMessage() != null){
                    Log.d(TAG, t.getMessage());
                }
            }
        });
        return invitationModels;
    }

    public void respondToInvitation(String inviteeUsername, String inviterUsername, boolean accept){
        Log.d(TAG, inviteeUsername + inviterUsername);
        api.respondInvitation(inviteeUsername, inviterUsername, accept).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful() && response.body() != null) {
                    try {
                        String message = response.body().string();
                        Log.d(TAG, message);
                        StyleableToast.makeText(context, message, R.style.custom_toast).show();
                    } catch (IOException e) {
                        Log.d(TAG, e.toString());
                    }
                }else{
                    try {
                        Log.d(TAG, response.errorBody().string());
                        Log.d(TAG, "Response Code:" + response.code());
                    } catch (IOException e) {
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
