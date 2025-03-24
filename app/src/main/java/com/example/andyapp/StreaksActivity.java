package com.example.andyapp;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.andyapp.utils.CalendarUtils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class StreaksActivity extends AppCompatActivity {

    private GridView calendarGridView;
    private TextView textMonthYear;
    private TextView dayCountText;
    private LocalDate currentDate;

    private Set<LocalDate> completedDates = new HashSet<>();
    private ImageButton btnback;

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

        calendarGridView = findViewById(R.id.calendarGridView);
        textMonthYear = findViewById(R.id.textMonthYear);
        dayCountText = findViewById(R.id.textDayCount);
        ImageButton prevButton = findViewById(R.id.buttonPreviousMonth);
        ImageButton nextButton = findViewById(R.id.buttonNextMonth);
        btnback = findViewById(R.id.btn_back);


        currentDate = LocalDate.now();

        loadSampleData();
        updateCalendar();

        prevButton.setOnClickListener(v -> {
            currentDate = currentDate.minusMonths(1);
            updateCalendar();
        });

        nextButton.setOnClickListener(v -> {
            currentDate = currentDate.plusMonths(1);
            updateCalendar();
        });
        btnback.setOnClickListener(view -> {
            Intent subActivityIntent = new Intent(view.getContext(), Dashboard.class);
            startActivity(subActivityIntent);
        });
    }

    private void loadSampleData() {
        // Simulating completed dates from Feb 1–17, 2025 except 18–22
        for (int i = 1; i <= 17; i++) {
            if (i <= 17) {
                completedDates.add(LocalDate.of(2025, 2, i));
            }
        }
        updateMonthStats();
    }

    private void updateCalendar() {
        // Set the month-year label
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.US);
        textMonthYear.setText(currentDate.format(formatter));

        // Generate days for the current month view
        List<LocalDate> monthDays = CalendarUtils.getMonthDates(currentDate);

        // Set the adapter
        CalendarAdapter adapter = new CalendarAdapter(this, monthDays, completedDates);
        calendarGridView.setAdapter(adapter);

        updateMonthStats();



        adapter.setOnDateClickListener(date -> {
            Toast.makeText(this, "Clicked: " + date.toString(), Toast.LENGTH_SHORT).show();
        });

        calendarGridView.setAdapter(adapter);

    }

    private void updateMonthStats() {
        int count = 0;
        for (LocalDate date : completedDates) {
            if (date.getMonth() == currentDate.getMonth() &&
                    date.getYear() == currentDate.getYear()) {
                count++;
            }
        }
        dayCountText.setText(String.valueOf(count));
    }
}
