package com.example.andyapp.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.R;
import com.example.andyapp.adapters.TransactionAdapter;
import com.example.andyapp.models.TransactionItem;
import com.example.andyapp.queries.ApiService;
import com.example.andyapp.queries.AuthRetrofitClient;
import com.example.andyapp.queries.mongoModels.Expense;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionsFragment extends Fragment {

    private List<TransactionItem> allItems = new ArrayList<>();
    private TransactionAdapter adapter;

    public TransactionsFragment() {
        super(R.layout.fragment_transaction);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        RecyclerView recyclerView = view.findViewById(R.id.transactionsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TransactionAdapter(new ArrayList<>());
        recyclerView.setAdapter(adapter);

        fetchExpensesFromBackend();
    }

    private void fetchExpensesFromBackend() {
        SharedPreferences prefs = getContext().getSharedPreferences("auth", Context.MODE_PRIVATE);
        String userId = prefs.getString("userId", "");
        String token = prefs.getString("token", "");

        if (userId.isEmpty() || token.isEmpty()) {
            Toast.makeText(getContext(), "Missing user ID or token. Please log in again.", Toast.LENGTH_SHORT).show();
            return;
        }

        ApiService api = AuthRetrofitClient.getApiService(token);
        api.getUserExpenses(userId).enqueue(new Callback<List<Expense>>() {
            @Override
            public void onResponse(Call<List<Expense>> call, Response<List<Expense>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<Expense> expenses = response.body();
                    allItems.clear();

                    for (Expense e : expenses) {
                        allItems.add(new TransactionItem(
                                "- $" + e.getAmount(),
                                e.getTitle() + " — " + e.getCategory(),
                                e.getCategory().toLowerCase().trim()
                        ));
                    }

                    adapter.updateData(allItems);
                } else {
                    Toast.makeText(getContext(), "Failed to load expenses (server error)", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<List<Expense>> call, Throwable t) {
                Toast.makeText(getContext(), "Network error: " + t.getMessage(), Toast.LENGTH_LONG).show();
                t.printStackTrace(); // Optional: view detailed error in Logcat
            }
        });
    }

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

    public String getShareableText() {
        StringBuilder builder = new StringBuilder();
        builder.append("📊 Transaction History:\n\n");
        for (TransactionItem item : adapter.getItems()) {
            builder.append(item.amount).append(" — ").append(item.description).append("\n");
        }
        return builder.toString();
    }

    public List<String> getShareableLines() {
        List<String> lines = new ArrayList<>();
        for (TransactionItem item : adapter.getItems()) {
            lines.add(item.amount + " — " + item.description);
        }
        return lines;
    }
}
