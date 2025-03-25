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

import java.util.ArrayList;
import java.util.List;

public class AlertAdapter extends RecyclerView.Adapter<AlertAdapter.AlertViewHolder> {

    private final List<AlertItem> alerts;

    public AlertAdapter(List<AlertItem> alerts) {
        this.alerts = new ArrayList<>(alerts);
    }

    public static class AlertViewHolder extends RecyclerView.ViewHolder {
        TextView title, subtitle;
        ImageView icon;

        public AlertViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.alertTitle);
            subtitle = itemView.findViewById(R.id.alertSubtitle);
            icon = itemView.findViewById(R.id.alertIcon);
        }
    }

    @NonNull
    @Override
    public AlertViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_alert, parent, false);
        return new AlertViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AlertViewHolder holder, int position) {
        AlertItem item = alerts.get(position);

        holder.title.setText(item.title);
        holder.subtitle.setText(item.subtitle);
        holder.icon.setImageResource(getIconForType(item.type));
    }

    @Override
    public int getItemCount() {
        return alerts.size();
    }

    private int getIconForType(String type) {
        switch (type.toLowerCase()) {
            case "money_request":
                return R.drawable.money_receive;
            case "reminder":
                return R.drawable.baseline_notifications_24;
            case "spend_alert":
                return R.drawable.warning_icon;
            default:
                return R.drawable.baseline_notifications_24;
        }
    }

    // 🔥 Allow fragment to update the adapter dynamically
    public void updateData(List<AlertItem> newAlerts) {
        alerts.clear();
        alerts.addAll(newAlerts);
        notifyDataSetChanged();
    }

    // Expose current visible items for sharing/export
    public List<AlertItem> getItems() {
        return new ArrayList<>(alerts);
    }
}
