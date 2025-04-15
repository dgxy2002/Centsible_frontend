package com.example.andyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.adapters.CalendarAdapter;
import com.example.andyapp.queries.ApiService;
import com.example.andyapp.queries.RetrofitClient;
import com.example.andyapp.queries.mongoModels.UserModel;
import com.example.andyapp.utils.CalendarUtils;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreaksActivity extends AppCompatActivity {

    private RecyclerView calendarRecyclerView;
    private TextView textMonthYear, dayCountText, streakNumberText, congratsMessage;
    private ImageButton btnBack;
    private LocalDate currentDate;

    private SharedPreferences mypref;
    private String userid;
    private Set<LocalDate> fullStreakDates = new HashSet<>();

    private static final String TAG = "STREAKS_LOG";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_streaks);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        currentDate = LocalDate.now();

        mypref = getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        userid = mypref.getString(LoginActivity.USERKEY, LoginActivity.DEFAULT_USERID);

        loadUserInfoAndUpdateStreak();
        setupListeners();
    }

    private void initViews() {
        calendarRecyclerView = findViewById(R.id.calendarRecyclerView);
        textMonthYear = findViewById(R.id.textMonthYear);
        dayCountText = findViewById(R.id.textDayCount);
        streakNumberText = findViewById(R.id.streakNumber);
        congratsMessage = findViewById(R.id.congratsMessage);
        btnBack = findViewById(R.id.btn_back);
    }

    private void setupListeners() {
        findViewById(R.id.buttonPreviousMonth).setOnClickListener(v -> {
            currentDate = currentDate.minusMonths(1);
            renderMonthView();
        });

        findViewById(R.id.buttonNextMonth).setOnClickListener(v -> {
            currentDate = currentDate.plusMonths(1);
            renderMonthView();
        });

        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(this, NavigationDrawerActivity.class));
        });
    }

    private void loadUserInfoAndUpdateStreak() {
        ApiService api = RetrofitClient.getApiService();
        api.getUserObject(userid).enqueue(new Callback<UserModel>() {
            @Override
            public void onResponse(Call<UserModel> call, Response<UserModel> response) {
                if (response.isSuccessful() && response.body() != null) {
                    int streakCount = response.body().getLoginStreak();
                    LocalDate today = LocalDate.now();
                    fullStreakDates.clear();

                    for (int i = 0; i < streakCount; i++) {
                        fullStreakDates.add(today.minusDays(i));
                    }

                    renderMonthView();
                    updateStreakHeaderUI(streakCount);
                } else {
                    Toast.makeText(StreaksActivity.this, "Failed to load streak data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UserModel> call, Throwable t) {
                Toast.makeText(StreaksActivity.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void renderMonthView() {
        Set<LocalDate> visibleStreakDates = new HashSet<>();
        for (LocalDate date : fullStreakDates) {
            if (date.getMonth() == currentDate.getMonth() && date.getYear() == currentDate.getYear()) {
                visibleStreakDates.add(date);
            }
        }

        textMonthYear.setText(currentDate.getMonth().name() + " " + currentDate.getYear());
        CalendarAdapter adapter = new CalendarAdapter(this, CalendarUtils.getMonthDates(currentDate), visibleStreakDates);
        calendarRecyclerView.setLayoutManager(new GridLayoutManager(this, 7));
        calendarRecyclerView.setAdapter(adapter);
    }

    private void updateStreakHeaderUI(int streakCount) {
        dayCountText.setText(String.valueOf(streakCount));
        streakNumberText.setText(String.valueOf(streakCount));

        if (streakCount == 0) {
            congratsMessage.setText("Let’s get back to it! Good practices take time.");
        } else if (streakCount <= 6) {
            congratsMessage.setText("Keep up the good work! 💪");
        } else {
            int weeks = streakCount / 7;
            congratsMessage.setText("🎉 You've kept a Perfect Streak for " + weeks + " straight " + (weeks == 1 ? "week" : "weeks") + ". Wow!");
        }
    }
}
