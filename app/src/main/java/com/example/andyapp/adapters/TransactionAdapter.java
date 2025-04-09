package com.example.andyapp.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.R;
import com.example.andyapp.models.TransactionItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private final List<TransactionItem> transactions;

    public TransactionAdapter(List<TransactionItem> transactions) {
        this.transactions = new ArrayList<>(transactions);
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView amount, description, date;
        ImageView icon;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.transactionAmount);
            description = itemView.findViewById(R.id.transactionDescription);
            icon = itemView.findViewById(R.id.transactionIcon);
            date = itemView.findViewById(R.id.transactionDate); // Ensure this ID exists in item_transaction.xml
        }
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        TransactionItem item = transactions.get(position);

        holder.amount.setText(item.amount);
        holder.description.setText(item.description);
        holder.icon.setImageResource(getIconForType(item.type));
        holder.date.setText(formatDate(item.date)); // 💡
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    public List<TransactionItem> getItems() {
        return new ArrayList<>(transactions);
    }

    public void updateData(List<TransactionItem> newItems) {
        transactions.clear();
        transactions.addAll(newItems);
        notifyDataSetChanged();
    }

    private int getIconForType(String type) {
        switch (type.toLowerCase()) {
            case "food":
                return R.drawable.baseline_fastfood_24;
            case "transport":
                return R.drawable.baseline_directions_bus_24;
            case "income":
                return R.drawable.dollar_icon;
            case "bills":
                return R.drawable.baseline_library_books_24;
            case "entertainment":
                return R.drawable.game_icon;
            default:
                return R.drawable.dollar_icon;
        }
    }

    private String formatDate(String raw) {
        if (raw == null || raw.trim().isEmpty()) return "Unknown date";

        String[] patterns = {
                "yyyy-MM-dd",
                "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'",
                "yyyy-MM-dd'T'HH:mm:ss.SSSXXX",
                "yyyy-MM-dd'T'HH:mm:ssXXX",
                "yyyy-MM-dd'T'HH:mm:ss"
        };

        for (String pattern : patterns) {
            try {
                SimpleDateFormat iso = new SimpleDateFormat(pattern, Locale.getDefault());
                Date date = iso.parse(raw);
                SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
                return output.format(date);
            } catch (ParseException e) {
                // ignore
            }
        }
        return "Unknown date";
    }
}
