package com.example.andyapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.models.LeaderboardUser;
import com.example.andyapp.queries.ApiService;
import com.example.andyapp.queries.RetrofitClient;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private SharedPreferences mypref;
    private String userId;

    private TextView firstName, firstPoints, secondName, secondPoints, thirdName, thirdPoints;
    private ImageView firstImg, secondImg, thirdImg;
    private RecyclerView recyclerView;
    private UsernameModelRCV adapter;
    private ArrayList<UsernameModel> usernameList = new ArrayList<>();

    private static final String TAG = "LEADERBOARD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leaderboard);

        mypref = getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        userId = mypref.getString(LoginActivity.USERKEY, LoginActivity.DEFAULT_USERID);
        Log.d(TAG, "Current userId: " + userId);

        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) -> {
            Log.e(TAG, "Uncaught exception in thread " + thread.getName(), throwable);
        });

        bindViews();
        fetchLeaderboard();
    }

    private void bindViews() {
        recyclerView = findViewById(R.id.leaderboard_recycler_view);
        adapter = new UsernameModelRCV(this, usernameList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        firstName = findViewById(R.id.first_place_name);
        secondName = findViewById(R.id.second_place_name);
        thirdName = findViewById(R.id.third_place_name);
        firstPoints = findViewById(R.id.first_place_points);
        secondPoints = findViewById(R.id.second_place_points);
        thirdPoints = findViewById(R.id.third_place_points);
        firstImg = findViewById(R.id.firstPlaceImageView);
        secondImg = findViewById(R.id.secondPlaceImageView);
        thirdImg = findViewById(R.id.imageView2);

        ImageButton btnBack = findViewById(R.id.btn_back);
        btnBack.setOnClickListener(v -> finish());

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void fetchLeaderboard() {
        Log.d(TAG, "Fetching leaderboard...");
        ApiService api = RetrofitClient.getApiService();
        api.getLeaderboard(userId).enqueue(new Callback<List<LeaderboardUser>>() {
            @Override
            public void onResponse(Call<List<LeaderboardUser>> call, Response<List<LeaderboardUser>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d(TAG, "Api call successful, users received: " + response.body().size());
                    updateLeaderboardUI(response.body());
                } else {
                    Log.e(TAG, "Leaderboard response empty or failed: " + response.code());
                }
            }

            @Override
            public void onFailure(Call<List<LeaderboardUser>> call, Throwable t) {
                Log.e(TAG, "Leaderboard fetch failed", t);
            }
        });
    }

    private void updateLeaderboardUI(List<LeaderboardUser> users) {
        Log.d(TAG, "Updating leaderboard UI");
        usernameList.clear();

        // Sort by score descending
        Collections.sort(users, (u1, u2) -> Integer.compare(u2.getScore(), u1.getScore()));

        // Top 1
        if (users.size() >= 1) {
            LeaderboardUser u = users.get(0);
            firstName.setText(userId.equals(u.getUserId()) ? "You" : u.getUsername());
            firstPoints.setText(String.valueOf(u.getScore()));
            Glide.with(this).load(u.getImageUrl()).placeholder(R.drawable.avatar).circleCrop().into(firstImg);
        }

        // Top 2
        if (users.size() >= 2) {
            LeaderboardUser u = users.get(1);
            secondName.setText(userId.equals(u.getUserId()) ? "You" : u.getUsername());
            secondPoints.setText(String.valueOf(u.getScore()));
            Glide.with(this).load(u.getImageUrl()).placeholder(R.drawable.avatar).circleCrop().into(secondImg);
        } else {
            secondName.setText("");
            secondPoints.setText("");
            secondImg.setImageDrawable(null);
        }

        // Top 3
        if (users.size() >= 3) {
            LeaderboardUser u = users.get(2);
            thirdName.setText(userId.equals(u.getUserId()) ? "You" : u.getUsername());
            thirdPoints.setText(String.valueOf(u.getScore()));
            Glide.with(this).load(u.getImageUrl()).placeholder(R.drawable.avatar).circleCrop().into(thirdImg);
        } else {
            thirdName.setText("");
            thirdPoints.setText("");
            thirdImg.setImageDrawable(null);
        }

        // Remaining users for RecyclerView
        List<LeaderboardUser> remainingUsers = users.size() > 3
                ? users.subList(3, users.size())
                : new ArrayList<>();

        for (LeaderboardUser user : remainingUsers) {
            String pointsText = String.valueOf(user.getScore());
            usernameList.add(new UsernameModel(
                    user.getUsername(),
                    pointsText,
                    "+0",
                    user.getImageUrl(), //using image URL
                    R.drawable.arrow_up,
                    user.getUserId()
            ));
        }

        adapter.notifyDataSetChanged();
    }

}
