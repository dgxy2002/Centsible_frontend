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

import java.util.ArrayList;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private final List<TransactionItem> transactions;

    public TransactionAdapter(List<TransactionItem> transactions) {
        this.transactions = new ArrayList<>(transactions);
    }

    public static class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView amount, description;
        ImageView icon;

        public TransactionViewHolder(@NonNull View itemView) {
            super(itemView);
            amount = itemView.findViewById(R.id.transactionAmount);
            description = itemView.findViewById(R.id.transactionDescription);
            icon = itemView.findViewById(R.id.transactionIcon);
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
    }

    @Override
    public int getItemCount() {
        return transactions.size();
    }

    // ✅ Expose current visible items (for share/export)
    public List<TransactionItem> getItems() {
        return new ArrayList<>(transactions);
    }

    // ✅ Allow list refresh (for filtering)
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
            case "game":
                return R.drawable.game_icon;
            default:
                return R.drawable.dollar_icon; // fallback icon
        }
    }
}
