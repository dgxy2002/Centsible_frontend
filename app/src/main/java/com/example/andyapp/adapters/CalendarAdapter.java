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
            DayOfWeek dow = date.getDayOfWeek();

            float corner = 100f;
            float[] radii;

            if (isStart && isEnd) {
                radii = new float[]{corner, corner, corner, corner, corner, corner, corner, corner};
            } else if (isStart) {
                radii = new float[]{corner, corner, 0f, 0f, 0f, 0f, corner, corner};
            } else if (isEnd) {
                radii = new float[]{0f, 0f, corner, corner, corner, corner, 0f, 0f};
            } else if (dow == DayOfWeek.SUNDAY) {
                radii = new float[]{corner, corner, 0f, 0f, 0f, 0f, corner, corner};
            } else if (dow == DayOfWeek.SATURDAY) {
                radii = new float[]{0f, 0f, corner, corner, corner, corner, 0f, 0f};
            } else {
                radii = new float[]{0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f};
            }

            //
            int curr = position % 7;
            int maxSteps = 7; // 7 days per week, max position in row = 6
            float fraction1 = (float) curr / maxSteps;
            float fraction2 = (float) (curr - 1) / maxSteps;

            int startColor = Color.parseColor("#F4BFDF"); // light pink
            int endColor = Color.parseColor("#D8BFF5");   // light purple

            int color1 = interpolateHSVColor(startColor, endColor, fraction1);
            int color2 = interpolateHSVColor(startColor, endColor, fraction2);

            GradientDrawable streakDrawable = new GradientDrawable(
                    GradientDrawable.Orientation.LEFT_RIGHT,
                    new int[]{color1, color2}
            );

            streakDrawable.setCornerRadii(radii);
            holder.dayText.setBackground(streakDrawable);
            holder.dayText.setTextColor(Color.BLACK);
        }
    }

    private int interpolateHSVColor(int colorStart, int colorEnd, float fraction) {
        float[] hsvStart = new float[3];
        float[] hsvEnd = new float[3];
        Color.colorToHSV(colorStart, hsvStart);
        Color.colorToHSV(colorEnd, hsvEnd);

        float[] hsvInterpolated = new float[3];
        for (int i = 0; i < 3; i++) {
            hsvInterpolated[i] = hsvStart[i] + fraction * (hsvEnd[i] - hsvStart[i]);
        }

        return Color.HSVToColor(hsvInterpolated);
    }


}
