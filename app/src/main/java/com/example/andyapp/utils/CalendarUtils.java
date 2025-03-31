package com.example.andyapp.utils;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;

public class CalendarUtils {
    public static List<LocalDate> getMonthDates(LocalDate date) {
        List<LocalDate> days = new ArrayList<>();
        YearMonth yearMonth = YearMonth.from(date);

        // First day of month
        LocalDate firstOfMonth = yearMonth.atDay(1);
        int dayOfWeek = firstOfMonth.getDayOfWeek().getValue(); // 1 = Monday, 7 = Sunday

        // Adjust to Sunday as the first column
        int startOffset = dayOfWeek % 7;

        for (int i = 0; i < startOffset; i++) {
            days.add(null); // empty cells
        }

        for (int day = 1; day <= yearMonth.lengthOfMonth(); day++) {
            days.add(yearMonth.atDay(day));
        }

        return days;
    }
}
