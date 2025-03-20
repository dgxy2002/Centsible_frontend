package com.example.andyapp;

import android.os.Bundle;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;

public class StreaksActivity extends AppCompatActivity {
    private TextView monthYearText;
    private GridView calendarGridView;
    private Calendar currentCalendar = Calendar.getInstance();
    private TextView dayCountText;
    private CalendarAdapter adapter;

    // Sample data - in a real app, you'd get this from a database or API
    private HashSet<Date> completedDates = new HashSet<>();

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

        // Initialize calendar views
        monthYearText = findViewById(R.id.textMonthYear);
        calendarGridView = findViewById(R.id.calendarGridView);
        dayCountText = findViewById(R.id.textDayCount);

        // Set up month navigation buttons
        ImageButton prevButton = findViewById(R.id.buttonPreviousMonth);
        ImageButton nextButton = findViewById(R.id.buttonNextMonth);

        prevButton.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, -1);
            updateCalendar();
        });

        nextButton.setOnClickListener(v -> {
            currentCalendar.add(Calendar.MONTH, 1);
            updateCalendar();
        });

        // Load sample data
        loadSampleData();

        // Initialize and update calendar
        updateCalendar();
    }

    private void loadSampleData() {
        // In a real app, this would be loaded from a database
        // This is just sample data for February 2025 as shown in the screenshot
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

            // Add completed days from Feb 1-17, 2025
            for (int i = 1; i <= 17; i++) {
                if (i != 18 && i != 19 && i != 20 && i != 21 && i != 22) { // Skip days that aren't completed
                    String dateStr = "2025-02-" + (i < 10 ? "0" + i : i);
                    completedDates.add(dateFormat.parse(dateStr));
                }
            }

            // Update stats display
            dayCountText.setText(String.valueOf(completedDates.size()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateCalendar() {
        // Set correct month and year title
        SimpleDateFormat formatter = new SimpleDateFormat("MMMM yyyy", Locale.US);
        monthYearText.setText(formatter.format(currentCalendar.getTime()));

        // List to hold all days for the grid
        ArrayList<Date> days = new ArrayList<>();

        // Clone the calendar to avoid modifying the original
        Calendar monthCalendar = (Calendar) currentCalendar.clone();
        monthCalendar.set(Calendar.DAY_OF_MONTH, 1);

        // Find which day of the week the 1st falls on (Sunday = 0, Monday = 1, ...)
        int firstDayOfMonth = monthCalendar.get(Calendar.DAY_OF_WEEK) - 1;

        // Move back to the first visible date in the grid
        monthCalendar.add(Calendar.DAY_OF_MONTH, -firstDayOfMonth);

        // Fill the grid with exactly 42 days (7 columns x 6 rows)
        for (int i = 0; i < 42; i++) {
            days.add(monthCalendar.getTime());
            monthCalendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        // Attach to adapter
        adapter = new CalendarAdapter(this, days, currentCalendar);
        adapter.setCompletedDays(completedDates);
        calendarGridView.setAdapter(adapter);
    }

    private void updateMonthStats() {
        // Count completed days in current month
        int completedDaysInMonth = 0;

        Calendar tempCal = Calendar.getInstance();
        for (Date date : completedDates) {
            tempCal.setTime(date);

            if (tempCal.get(Calendar.MONTH) == currentCalendar.get(Calendar.MONTH) &&
                    tempCal.get(Calendar.YEAR) == currentCalendar.get(Calendar.YEAR)) {
                completedDaysInMonth++;
            }
        }

        // Update stats display
        dayCountText.setText(String.valueOf(completedDaysInMonth));
    }
}