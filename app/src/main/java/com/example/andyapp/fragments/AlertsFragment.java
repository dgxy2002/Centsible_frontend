package com.example.andyapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.LoginActivity;
import com.example.andyapp.R;
import com.example.andyapp.adapters.AlertAdapter;
import com.example.andyapp.models.AlertItem;
import com.example.andyapp.models.NotificationResponse;
import com.example.andyapp.queries.ApiService;
import com.example.andyapp.queries.RetrofitClient;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import io.github.muddz.styleabletoast.StyleableToast;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertsFragment extends Fragment {

    private List<AlertItem> allAlerts = new ArrayList<>();
    private AlertAdapter adapter;

    private SharedPreferences mypref;
    private String userId;
    private String token;

    private static final String TAG = "ALERTS_LOG";

    public AlertsFragment() {
        super(R.layout.fragment_alerts);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.alertsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AlertAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        // Load userId/token using sean's sharedpref
        mypref = requireContext().getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        userId = mypref.getString(LoginActivity.USERKEY, LoginActivity.DEFAULT_USERID);
        token = mypref.getString(LoginActivity.TOKENKEY, "None");

        Log.d(TAG, "UserID: " + userId);
        Log.d(TAG, "Token: " + token);

        fetchUnreadNotificationCount();
        fetchNotificationsFromBackend();
    }

    private void fetchUnreadNotificationCount() {
        if (userId.isEmpty() || token.equals("None")) {
            //StyleableToast.makeText(getContext(), "Missing ID or token", R.style.custom_toast).show();
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            ApiService api = RetrofitClient.getApiService();
            api.getUnreadCount(token, userId).enqueue(new Callback<Integer>() {
                @Override
                public void onResponse(Call<Integer> call, Response<Integer> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        int count = response.body();
                        if (count > 0) {
                            StyleableToast.makeText(getContext(), "🔴 " + count + " Unread Alert(s)", R.style.custom_toast).show();
                        }
                    }
                }

                @Override
                public void onFailure(Call<Integer> call, Throwable t) {
                    StyleableToast.makeText(getContext(), "Failed to load unread count", R.style.custom_toast).show();
                }
            });
        });
    }

    private void fetchNotificationsFromBackend() {
        if (userId.isEmpty() || token.equals("None")) {
            //StyleableToast.makeText(getContext(), "Missing ID or token", R.style.custom_toast).show();
            return;
        }

        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            ApiService api = RetrofitClient.getApiService();
            api.getNotifications(token, userId).enqueue(new Callback<List<NotificationResponse>>() {
                @Override
                public void onResponse(Call<List<NotificationResponse>> call, Response<List<NotificationResponse>> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        allAlerts.clear();
                        for (NotificationResponse notif : response.body()) {
                            Log.d(TAG, "Alert: " + notif.getMessage() + " | Type: " + notif.getType());

                            String type = notif.getType();
                            if (type == null) {
                                // manual fallback as there is no type returned from backend
                                if (notif.getMessage() != null && notif.getMessage().toLowerCase().startsWith("warning")) {
                                    type = "warning";
                                } else {
                                    type = "notification";
                                }
                            }

                            allAlerts.add(new AlertItem(
                                    notif.getMessage(),
                                    "By " + notif.getSenderUsername() + "\nOn " + notif.getCreatedAt(),
                                    type,
                                    notif.isRead()
                            ));
                        }
                        adapter.updateData(allAlerts);
                    } else {
                        StyleableToast.makeText(getContext(), "Failed to load alerts", R.style.custom_toast).show();
                    }
                }

                @Override
                public void onFailure(Call<List<NotificationResponse>> call, Throwable t) {
                    Log.e(TAG, "Network call failed", t);
                    StyleableToast.makeText(getContext(), "Network error: " + t.getMessage(), R.style.custom_toast).show();
                    t.printStackTrace();
                }
            });
        });
    }

    public List<String> getShareableLines() {
        List<String> lines = new ArrayList<>();
        for (AlertItem item : adapter.getItems()) {
            lines.add(item.title + " — " + item.subtitle);
        }
        return lines;
    }
}
