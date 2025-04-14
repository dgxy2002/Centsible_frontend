package com.example.andyapp.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.R;
import com.example.andyapp.models.AlertItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.ViewHolder> {

    private List<AlertItem> alertList;
    private static final String TAG = "ALERT_DATE_PARSE";

    public AlertAdapter(List<AlertItem> alertList) {
        this.alertList = alertList;
    }

    public void updateData(List<AlertItem> newAlerts) {
        this.alertList = newAlerts;
        notifyDataSetChanged();
    }

    public List<AlertItem> getItems() {
        return alertList;
    }

    @NonNull
    @Override
    public AlertAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.alert_recycler_view_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertAdapter.ViewHolder holder, int position) {
        AlertItem alert = alertList.get(position);

        holder.title.setText(alert.getTitle());

        // Split subtitle
        String[] parts = alert.getSubtitle().split("\n");
        if (parts.length >= 2) {
            holder.sender.setText(parts[0]);
            holder.date.setText(formatDate(parts[1]));
        } else {
            holder.sender.setText("");
            holder.date.setText("");
        }

        // Set icon
        holder.icon.setImageResource(getIconForAlertType(alert.getType()));

        // Tint background for warning
        if ("warning".equalsIgnoreCase(alert.getType())) {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.warning_tint));
        } else {
            holder.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), android.R.color.transparent));
        }
    }

    private int getIconForAlertType(String type) {
        if (type == null) return R.drawable.baseline_notifications_24;

        switch (type.toLowerCase()) {
            case "food":
                return R.drawable.dining;
            case "warning":
                return R.drawable.warning_icon;
            case "money request":
                return R.drawable.money_receive;
            case "notification":
                return R.drawable.bell_icon_new;
            case "transport":
                return R.drawable.baseline_directions_bus_24;
            case "expense":
                return R.drawable.dollar_icon;
            default:
                return R.drawable.bell_icon_new;
        }
    }

    @Override
    public int getItemCount() {
        return alertList != null ? alertList.size() : 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView icon;
        TextView title, sender, date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            icon = itemView.findViewById(R.id.alertIcon);
            title = itemView.findViewById(R.id.alertTitle);
            sender = itemView.findViewById(R.id.alertSender);
            date = itemView.findViewById(R.id.alertDate);
        }
    }

    private String formatDate(String raw) {
        if (raw == null || raw.trim().isEmpty()) return "";

        if (raw.startsWith("On ")) raw = raw.substring(3).trim();

        String[] patterns = {
                "yyyy-MM-dd'T'HH:mm:ss.SSS",
                "yyyy-MM-dd'T'HH:mm:ss",
                "yyyy-MM-dd"
        };

        for (String pattern : patterns) {
            try {
                SimpleDateFormat iso = new SimpleDateFormat(pattern, Locale.getDefault());
                Date date = iso.parse(raw);
                SimpleDateFormat display = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
                return "On " + display.format(date);
            } catch (ParseException e) {
                Log.d(TAG, "Pattern failed: " + pattern + " | raw: " + raw);
            }
        }

        Log.w(TAG, "Date parsing failed for raw string: " + raw);
        return "Unknown Date";
    }
}
