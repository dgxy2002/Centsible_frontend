package com.example.andyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.andyapp.adapters.CalendarAdapter;
import com.example.andyapp.queries.ApiService;
import com.example.andyapp.queries.RetrofitClient;
import com.example.andyapp.queries.mongoModels.Expense;
import com.example.andyapp.utils.CalendarUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StreaksActivity extends AppCompatActivity {

    private GridView calendarGridView;
    private TextView textMonthYear, dayCountText, streakNumberText, congratsMessage;
    private ImageButton btnBack;
    private LocalDate currentDate;

    private SharedPreferences mypref;
    private String userid, token;
    private Set<LocalDate> completedDates = new HashSet<>();

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
        token = mypref.getString(LoginActivity.TOKENKEY, "None");

        Log.d(TAG, "UserID: " + userid);
        Log.d(TAG, "Token: " + token);

        loadExpenseDatesFromBackend();
        updateCalendar();
        setupListeners();
    }

    private void initViews() {
        calendarGridView = findViewById(R.id.calendarGridView);
        textMonthYear = findViewById(R.id.textMonthYear);
        dayCountText = findViewById(R.id.textDayCount);
        streakNumberText = findViewById(R.id.streakNumber);
        congratsMessage = findViewById(R.id.congratsMessage);
        btnBack = findViewById(R.id.btn_back);
    }

    private void setupListeners() {
        findViewById(R.id.buttonPreviousMonth).setOnClickListener(v -> {
            currentDate = currentDate.minusMonths(1);
            updateCalendar();
        });

        findViewById(R.id.buttonNextMonth).setOnClickListener(v -> {
            currentDate = currentDate.plusMonths(1);
            updateCalendar();
        });

        btnBack.setOnClickListener(view -> {
            startActivity(new Intent(this, NavigationDrawerActivity.class));
        });
    }

    private void loadExpenseDatesFromBackend() {
        if (userid == null || userid.isEmpty() || token == null || token.equals("None")) {
            Toast.makeText(this, "Missing user ID or token. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = RetrofitClient.getApiService();
        api.getUserExpenses(token, userid).enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    completedDates.clear();
                    for (Expense expense : response.body()) {
                        try {
                            completedDates.add(expense.getParsedCreatedDate());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    updateCalendar();
                } else {
                    Toast.makeText(StreaksActivity.this, "Failed to load expense dates", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {
                Toast.makeText(StreaksActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace();
            }
        });
    }

    private void updateCalendar() {
        textMonthYear.setText(currentDate.format(DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US)));
        List<LocalDate> monthDays = CalendarUtils.getMonthDates(currentDate);
        CalendarAdapter adapter = new CalendarAdapter(this, monthDays, completedDates);
        calendarGridView.setAdapter(adapter);

        adapter.setOnDateClickListener(date -> {
            Toast.makeText(this, "Clicked: " + date.toString(), Toast.LENGTH_SHORT).show();
        });

        updateMonthStats();
    }

    private void updateMonthStats() {
        int count = 0;
        LocalDate today = LocalDate.now();
        LocalDate current = today;

        // Sort the dates
        List<LocalDate> sortedDates = new ArrayList<>(completedDates);
        Collections.sort(sortedDates, Collections.reverseOrder()); // Descending order

        Set<LocalDate> dateSet = new HashSet<>(sortedDates);

        while (dateSet.contains(current)) {
            count++;
            current = current.minusDays(1);
        }

        // Update UI
        dayCountText.setText(String.valueOf(count));
        streakNumberText.setText(String.valueOf(count));

        // Show congrats based on streak
        if (count == 0) {
            congratsMessage.setText("Let’s get back to it! Good practices take time.");
        } else if (count <= 6) {
            congratsMessage.setText("Keep up the good work! 💪");
        } else {
            int weeks = count / 7;
            congratsMessage.setText("🎉 You've kept a Perfect Streak for " + weeks + " straight " + (weeks == 1 ? "week" : "weeks") + ". Wow!");
        }
    }

}
