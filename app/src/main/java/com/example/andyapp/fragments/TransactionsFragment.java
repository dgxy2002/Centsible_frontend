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
import com.example.andyapp.adapters.TransactionAdapter;
import com.example.andyapp.models.TransactionItem;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TransactionsFragment extends Fragment {

    private List<TransactionItem> allItems = new ArrayList<>();
    private TransactionAdapter adapter;

    public TransactionsFragment() {
        super(R.layout.fragment_transaction); // ✅ ensure layout file is correct
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.transactionsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        allItems = Arrays.asList(
                new TransactionItem("+ $20.00", "extra cash from mahjong wins", "income"),
                new TransactionItem("- $3.60", "chicken rice lunch", "food"),
                new TransactionItem("- $22.50", "grab to cricket game", "transport"),
                new TransactionItem("- $18.00", "supper with the bois", "food"),
                new TransactionItem("- $36.00", "Auto: Phone Bills", "bills")
        );

        adapter = new TransactionAdapter(new ArrayList<>(allItems));
        recyclerView.setAdapter(adapter);
    }

    // Called from Activity for tab-specific filter
    public void showFilterDialog() {
        String[] categories = {"All", "income", "food", "transport", "bills", "game"};
        new AlertDialog.Builder(getContext())
                .setTitle("Filter Transactions")
                .setItems(categories, (dialog, which) -> {
                    String selected = categories[which];
                    if (selected.equals("All")) {
                        adapter.updateData(allItems);
                    } else {
                        List<TransactionItem> filtered = new ArrayList<>();
                        for (TransactionItem item : allItems) {
                            if (item.type.equalsIgnoreCase(selected)) {
                                filtered.add(item);
                            }
                        }
                        adapter.updateData(filtered);
                    }
                })
                .show();
    }

    // For sharing via text intent
    public String getShareableText() {
        StringBuilder builder = new StringBuilder();
        builder.append("📊 Transaction History:\n\n");
        for (TransactionItem item : adapter.getItems()) {
            builder.append(item.amount).append(" — ").append(item.description).append("\n");
        }
        return builder.toString();
    }

    // For exporting to PDF
    public List<String> getShareableLines() {
        List<String> lines = new ArrayList<>();
        for (TransactionItem item : adapter.getItems()) {
            lines.add(item.amount + " — " + item.description);
        }
        return lines;
    }
}
