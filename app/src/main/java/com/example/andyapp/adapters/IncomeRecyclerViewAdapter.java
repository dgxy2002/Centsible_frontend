package com.example.andyapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.DataObserver;
import com.example.andyapp.R;
import com.example.andyapp.models.FetchIncome;
import com.example.andyapp.queries.FetchIncomes;

import java.util.ArrayList;

public class IncomeRecyclerViewAdapter extends RecyclerView.Adapter<IncomeRecyclerViewAdapter.MyViewHolder> implements DataObserver<FetchIncomes> {

    Context context;
    ArrayList<FetchIncome> fetchIncomeArrayList;

    public IncomeRecyclerViewAdapter(Context context, ArrayList<FetchIncome> fetchIncomeArrayList ) {
        this.fetchIncomeArrayList = fetchIncomeArrayList;
        this.context = context;
    }

    @NonNull
    @Override
    public IncomeRecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.income_recycler_view_row, parent, false);
        return new IncomeRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IncomeRecyclerViewAdapter.MyViewHolder holder, int position) {
        FetchIncome model = fetchIncomeArrayList.get(position);
        holder.iconView.setImageResource(R.drawable.dining);
        holder.amountView.setText(String.format("$%s", model.getAmount()));
        holder.dateView.setText(model.getCreatedDate());
        holder.titleView.setText(model.getTitle());
    }

    @Override
    public int getItemCount() {
        return fetchIncomeArrayList.size();
    }

    @Override
    public void updateData(FetchIncomes data) {
        //Add logic here
        this.fetchIncomeArrayList = data.getFetchIncomeArrayList();
        notifyDataSetChanged();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView iconView;
        private TextView titleView;
        private TextView dateView;
        private TextView amountView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iconView = itemView.findViewById(R.id.incomeIconView);
            titleView = itemView.findViewById(R.id.incomeTitleView);
            dateView = itemView.findViewById(R.id.incomeDateView);
            amountView = itemView.findViewById(R.id.incomeAmountView);
        }
    }
}
