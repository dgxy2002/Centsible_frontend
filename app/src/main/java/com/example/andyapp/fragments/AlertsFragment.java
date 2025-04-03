package com.example.andyapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.R;
import com.example.andyapp.adapters.AlertAdapter;
import com.example.andyapp.models.AlertItem;
import com.example.andyapp.models.NotificationResponse;
import com.example.andyapp.queries.ApiService;
import com.example.andyapp.queries.RetrofitClient;
import com.example.andyapp.LoginActivity;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AlertsFragment extends Fragment {

    private List<AlertItem> allAlerts = new ArrayList<>();
    private AlertAdapter adapter;

    public AlertsFragment() {
        super(R.layout.fragment_alerts);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.alertsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new AlertAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        fetchUnreadNotificationCount();
        fetchNotificationsFromBackend();
    }

    private void fetchUnreadNotificationCount() {
        SharedPreferences prefs = getContext().getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        String userId = prefs.getString(LoginActivity.USERKEY, "");
        String token = prefs.getString(LoginActivity.TOKENKEY, "");

        if (userId.isEmpty() || token.isEmpty()) return;

        ApiService api = RetrofitClient.getApiService();
        api.getUnreadCount("Bearer " + token, userId).enqueue(new Callback<Integer>() {
            @Override
            public void onResponse(Call<Integer> call, Response<Integer> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int count = response.body();
                    if (count > 0) {
                        Toast.makeText(getContext(), "🔴 " + count + " unread alert(s)", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<Integer> call, Throwable t) {
                Toast.makeText(getContext(), "Failed to load unread count", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void fetchNotificationsFromBackend() {
        SharedPreferences prefs = getContext().getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        String userId = prefs.getString(LoginActivity.USERKEY, "");
        String token = prefs.getString(LoginActivity.TOKENKEY, "");

        if (userId.isEmpty() || token.isEmpty()) return;

        ApiService api = RetrofitClient.getApiService();
        api.getNotifications("Bearer " + token, userId).enqueue(new Callback<List<NotificationResponse>>() {
            @Override
            public void onResponse(Call<List<NotificationResponse>> call, Response<List<NotificationResponse>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    allAlerts.clear();

                    for (NotificationResponse notif : response.body()) {
                        allAlerts.add(new AlertItem(
                                notif.getMessage(),
                                "By " + notif.getSenderUsername() + "\nOn " + notif.getCreatedAt(),
                                "notification",
                                notif.isRead()
                        ));
                    }

                    adapter.updateData(allAlerts);
                } else {
                    Toast.makeText(getContext(), "Failed to load alerts", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<NotificationResponse>> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
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
