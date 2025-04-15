package com.example.andyapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.R;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public class CalendarAdapter extends RecyclerView.Adapter<CalendarAdapter.ViewHolder> {

    private final Context context;
    private final List<LocalDate> days;
    private final Set<LocalDate> highlightedDates;
    private OnDateClickListener listener;

    public CalendarAdapter(Context context, List<LocalDate> days, Set<LocalDate> highlightedDates) {
        this.context = context;
        this.days = days;
        this.highlightedDates = highlightedDates;
    }

    public interface OnDateClickListener {
        void onClick(LocalDate date);
    }

    public void setOnDateClickListener(OnDateClickListener listener) {
        this.listener = listener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView dayText;

        public ViewHolder(View itemView) {
            super(itemView);
            dayText = itemView.findViewById(R.id.dayCellText);
            itemView.setOnClickListener(v -> {
                if (listener != null) {
                    LocalDate date = days.get(getAdapterPosition());
                    if (date != null) listener.onClick(date);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return days.size();
    }

    @Override
    public CalendarAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.day_cell_layout, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(CalendarAdapter.ViewHolder holder, int position) {
        LocalDate date = days.get(position);

        if (date == null) {
            holder.dayText.setText("");
            holder.dayText.setBackground(null);
            return;
        }

        holder.dayText.setText(String.valueOf(date.getDayOfMonth()));
        holder.dayText.setTextColor(ContextCompat.getColor(context, R.color.black));
        holder.dayText.setBackground(null);

        LocalDate today = LocalDate.now();

        if (highlightedDates.contains(date) && !date.isAfter(today)) {
            boolean isStart = !highlightedDates.contains(date.minusDays(1));
            boolean isEnd = !highlightedDates.contains(date.plusDays(1));

            GradientDrawable streakDrawable = new GradientDrawable();
            streakDrawable.setColor(ContextCompat.getColor(context, R.color.streak_active)); // Default streak color

            // Weekend check
            DayOfWeek day = date.getDayOfWeek();
            if (day == DayOfWeek.SATURDAY || day == DayOfWeek.SUNDAY) {
                streakDrawable.setColor(ContextCompat.getColor(context, R.color.streak_weekend)); // Optional alt color
            }

            if (isStart && isEnd) {
                streakDrawable.setCornerRadius(100f); // Circle
            } else if (isStart) {
                streakDrawable.setCornerRadii(new float[]{100f, 100f, 0f, 0f, 0f, 0f, 100f, 100f}); // Left rounded
            } else if (isEnd) {
                streakDrawable.setCornerRadii(new float[]{0f, 0f, 100f, 100f, 100f, 100f, 0f, 0f}); // Right rounded
            } else {
                streakDrawable.setCornerRadius(0f); // Square middle
            }

            holder.dayText.setBackground(streakDrawable);
            holder.dayText.setTextColor(Color.WHITE);
        }
    }
}
