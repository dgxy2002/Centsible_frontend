package com.example.andyapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.R;
import com.example.andyapp.models.LogBudgetModel;

import java.util.ArrayList;


public class LogBd_RecyclerViewAdapter extends RecyclerView.Adapter<LogBd_RecyclerViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<LogBudgetModel> logBudgetModels;
    public int selectedItemPosition = 0;

    public LogBd_RecyclerViewAdapter(Context context, ArrayList<LogBudgetModel> logBudgetModels) {
        this.context = context;
        this.logBudgetModels = logBudgetModels;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.log_budget_recycler_view_row, parent, false);
        return new LogBd_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        position = position % logBudgetModels.size();
        LogBudgetModel logBudgetModel = logBudgetModels.get(position);
        String category = logBudgetModel.getCategory();
        int icon = logBudgetModel.getIcon();
        holder.imageView.setImageResource(icon);
        holder.textView.setText(category);
    }

    @Override
    public int getItemCount() {
        return Integer.MAX_VALUE;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        ImageView imageView;

        public MyViewHolder(@NonNull View itemView){
            super(itemView);
            textView = itemView.findViewById(R.id.logBdCategoryView);
            imageView = itemView.findViewById(R.id.logBdImageView);
        }

    }
}
