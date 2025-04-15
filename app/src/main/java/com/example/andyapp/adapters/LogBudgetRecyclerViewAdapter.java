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
import com.example.andyapp.RecyclerViewOnClickInterface;
import com.example.andyapp.models.LogBudgetModel;
import com.example.andyapp.models.LogBudgetModels;

import java.util.ArrayList;


public class LogBudgetRecyclerViewAdapter extends RecyclerView.Adapter<LogBudgetRecyclerViewAdapter.MyViewHolder> implements DataObserver<LogBudgetModels> {
    Context context;
    ArrayList<LogBudgetModel> logBudgetModels;
    private final RecyclerViewOnClickInterface recyclerViewInterface;
    String TAG = "LOGCAT";


    public LogBudgetRecyclerViewAdapter(Context context, ArrayList<LogBudgetModel> logBudgetModels, RecyclerViewOnClickInterface recyclerViewInterface) {
        this.context = context;
        this.logBudgetModels = logBudgetModels;
        this.recyclerViewInterface = recyclerViewInterface;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.log_budget_recycler_view_row, parent, false);
        return new LogBudgetRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        LogBudgetModel logBudgetModel = logBudgetModels.get(position);
        String category = logBudgetModel.getCategory();
        int icon = logBudgetModel.getIcon();
        String budget = logBudgetModel.getBudget();
        double budgetDecimal = Double.parseDouble(budget);
        int id = logBudgetModel.getId();
        //Initialise Views
        holder.imageView.setImageResource(icon);
        holder.categoryTextView.setText(category);
        holder.budgetTextView.setText(String.format("$%.2f", budgetDecimal));
    }

    @Override
    public int getItemCount() {
        return logBudgetModels.size();
    }

    @Override
    public void updateData(LogBudgetModels data) {
        this.logBudgetModels = data.getLogBudgetModels();
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView categoryTextView;
        ImageView imageView;
        TextView budgetTextView;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            categoryTextView = itemView.findViewById(R.id.logBdCategoryView);
            imageView = itemView.findViewById(R.id.logBdImageView);
            budgetTextView = itemView.findViewById(R.id.catBudgetTextView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (recyclerViewInterface != null){
                        int position = getBindingAdapterPosition();
                        if (position != RecyclerView.NO_POSITION){
                            recyclerViewInterface.onItemClick(position);
                        }
                    }
                }
            });
        }

    }
}
