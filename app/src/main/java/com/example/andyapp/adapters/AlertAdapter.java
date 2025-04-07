package com.example.andyapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
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
                .inflate(R.layout.item_alert, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertAdapter.ViewHolder holder, int position) {
        AlertItem alert = alertList.get(position);

        holder.title.setText(alert.getTitle());

        // Split the subtitle into sender and datetime
        String[] parts = alert.getSubtitle().split("\n");
        if (parts.length >= 2) {
            holder.sender.setText(parts[0]); 
            holder.date.setText(formatDate(parts[1])); // ISO to readable format
        } else {
            holder.sender.setText("");
            holder.date.setText("");
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
        String[] patterns = {
                "yyyy-MM-dd'T'HH:mm:ss.SSS",    // Full ISO with milliseconds
                "yyyy-MM-dd'T'HH:mm:ss"          // ISO without milliseconds
        };

        for (String pattern : patterns) {
            try {
                SimpleDateFormat iso = new SimpleDateFormat(pattern, Locale.getDefault());
                Date date = iso.parse(raw);
                SimpleDateFormat display = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
                return "On " + display.format(date);
            } catch (ParseException ignored) {
            }
        }

        return "not working"; // fallback if all formats fail
    }

}
