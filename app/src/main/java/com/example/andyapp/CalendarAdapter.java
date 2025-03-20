package com.example.andyapp;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;

public class CalendarAdapter extends ArrayAdapter<Date> {
    private Context context;
    private HashSet<Date> completedDays = new HashSet<>();
    private LayoutInflater inflater;
    private Calendar currentMonthCalendar;

    public CalendarAdapter(Context context, ArrayList<Date> days, Calendar currentMonthCalendar) {
        super(context, R.layout.calendar_day_item, days);
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.currentMonthCalendar = currentMonthCalendar;
    }

    public void setCompletedDays(HashSet<Date> completedDays) {
        this.completedDays = completedDays;
        notifyDataSetChanged();
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Date being displayed
        Date date = getItem(position);

        if (convertView == null) {
            convertView = inflater.inflate(R.layout.calendar_day_item, parent, false);
        }
        TextView dateText = convertView.findViewById(R.id.dateText);

        // Clear styling
        dateText.setBackgroundResource(0);

        if (date != null) {
            // Set the day number
            Calendar calendar = Calendar.getInstance();
            calendar.setTime(date);
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            dateText.setText(String.valueOf(day));

            // Check if date is in the current month
            int displayMonth = currentMonthCalendar.get(Calendar.MONTH);
            int currentMonth = calendar.get(Calendar.MONTH);

            if (displayMonth != currentMonth) {
                // Hide dates from previous/next month properly
                dateText.setText("");
                dateText.setVisibility(View.INVISIBLE);
            } else {
                // Ensure visible if it's the correct month
                dateText.setVisibility(View.VISIBLE);
                dateText.setText(String.valueOf(day));

                // Check if this date is completed
                boolean isCompleted = completedDays.contains(date);

                if (isCompleted) {
                    dateText.setBackgroundResource(R.drawable.calendar_day_drawable);
                    dateText.setTextColor(Color.WHITE);
                } else {
                    dateText.setTextColor(Color.BLACK); // Set black only for non-completed days
                }
            }
        }

        return convertView;
    }

}