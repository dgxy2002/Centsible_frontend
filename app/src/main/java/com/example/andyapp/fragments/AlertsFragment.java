package com.example.andyapp.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.R;
import com.example.andyapp.adapters.AlertAdapter;
import com.example.andyapp.models.AlertItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AlertsFragment extends Fragment {

    private List<AlertItem> allAlerts = new ArrayList<>();
    private AlertAdapter adapter;

    public AlertsFragment() {
        super(R.layout.fragment_alerts);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.alertsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // sample data set
        allAlerts = Arrays.asList(
                new AlertItem("Hugo spent $100", "Merchant: Wild Rift", "spend_alert"),
                new AlertItem("Tim requests for more money", "Tim's budget left: $1.50", "money_request"),
                new AlertItem("Pay Utility Bills!", "Auto: Reminder", "reminder"),
                new AlertItem("Ahmama spent $5000", "Merchant: Best Jade Shop Pte Ltd", "spend_alert")
        );

        adapter = new AlertAdapter(new ArrayList<>(allAlerts));
        recyclerView.setAdapter(adapter);
    }

    // Filter Dialog
    public void showFilterDialog() {
        String[] categories = {"All", "spend_alert", "money_request", "reminder"};
        new AlertDialog.Builder(getContext())
                .setTitle("Filter Alerts")
                .setItems(categories, (dialog, which) -> {
                    String selected = categories[which];
                    if (selected.equals("All")) {
                        adapter.updateData(allAlerts);
                    } else {
                        List<AlertItem> filtered = new ArrayList<>();
                        for (AlertItem item : allAlerts) {
                            if (item.type.equalsIgnoreCase(selected)) {
                                filtered.add(item);
                            }
                        }
                        adapter.updateData(filtered);
                    }
                })
                .show();
    }
    //below this all completely chat i also dk wat this is can remove uh honeslty
    // ✅ Share plain text
    public String getShareableText() {
        StringBuilder builder = new StringBuilder();
        builder.append("🚨 Alerts Summary:\n\n");
        for (AlertItem item : adapter.getItems()) {
            builder.append(item.title).append(" — ").append(item.subtitle).append("\n");
        }
        return builder.toString();
    }

    // ✅ Shareable PDF line format
    public List<String> getShareableLines() {
        List<String> lines = new ArrayList<>();
        for (AlertItem item : adapter.getItems()) {
            lines.add(item.title + " — " + item.subtitle);
        }
        return lines;
    }
}
