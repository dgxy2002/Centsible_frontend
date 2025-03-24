package com.example.andyapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.*;
import android.widget.BaseAdapter;
import android.widget.TextView;
import androidx.core.content.ContextCompat;

import com.example.andyapp.R;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class CalendarAdapter extends BaseAdapter {

    private final Context context;
    private final List<LocalDate> days;
    private final Set<LocalDate> completedDates;
    private OnDateClickListener listener;

    public interface OnDateClickListener {
        void onDateClick(LocalDate date);
    }

    public void setOnDateClickListener(OnDateClickListener listener) {
        this.listener = listener;
    }

    public CalendarAdapter(Context context, List<LocalDate> days, Set<LocalDate> completedDates) {
        this.context = context;
        this.days = days;
        this.completedDates = completedDates;
    }

    @Override
    public int getCount() {
        return days.size();
    }

    @Override
    public Object getItem(int i) {
        return days.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LocalDate date = days.get(i);
        @SuppressLint("ViewHolder") View view = LayoutInflater.from(context).inflate(R.layout.calendar_cell, parent, false);
        TextView dayText = view.findViewById(R.id.calendar_day_text);

        if (date != null) {
            dayText.setText(String.valueOf(date.getDayOfMonth()));

            if (completedDates.contains(date)) {
                view.setBackgroundResource(R.drawable.streak_gradient_background);
            }

            if (date.equals(LocalDate.now())) {
                view.setBackgroundResource(R.drawable.today_background);
                dayText.setTextColor(ContextCompat.getColor(context, android.R.color.white));
            }

            view.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onDateClick(date);
                }
            });
        } else {
            dayText.setText("");
            view.setClickable(false);
        }

        return view;
    }
}
