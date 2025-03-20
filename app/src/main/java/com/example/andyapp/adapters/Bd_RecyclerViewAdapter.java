package com.example.andyapp.adapters;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.R;
import com.example.andyapp.models.BudgetModel;

import java.util.ArrayList;

public class Bd_RecyclerViewAdapter extends RecyclerView.Adapter<Bd_RecyclerViewAdapter.MyViewHolder> {

    Context context;
    ArrayList<BudgetModel> budgetModels;
    float totalBudget;

    public Bd_RecyclerViewAdapter(Context context, ArrayList<BudgetModel> budgetModels) {
        this.context = context;
        this.budgetModels = budgetModels;
        this.totalBudget = 0;
        for (int i = 0; i < budgetModels.size(); i++){
            this.totalBudget += budgetModels.get(i).getBudget();
        }
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflates the layout
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.budget_recycler_view_row, parent, false);
        return new Bd_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageView.setImageResource(budgetModels.get(position).getImage());
        holder.catView.setText(budgetModels.get(position).getCategory());
        holder.amtView.setText(String.format("%.2f / %.2f", budgetModels.get(position).getSpent(), budgetModels.get(position).getBudget()));
        holder.progressBar.setProgress(budgetModels.get(position).getProgress());
//        ViewGroup.LayoutParams pgblayout = holder.progressBar.getLayoutParams();
//        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//        pgblayout.width = (int) ((budgetModels.get(position).getBudget()/ totalBudget) * 3000);
//        pgblayout.width = Math.min(pgblayout.width, 1000);
//        pgblayout.width = Math.max(pgblayout.width, 150);
//        holder.progressBar.setLayoutParams(pgblayout);
    }

    @Override
    public int getItemCount() {
        return budgetModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //grabbing views from recycler_row_view.xml file
        ImageView imageView;
        TextView amtView, catView;
        ProgressBar progressBar;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.bdiconView);
            catView = itemView.findViewById(R.id.bdcategoryView);
            amtView = itemView.findViewById(R.id.bdamtView);
            progressBar = itemView.findViewById(R.id.budgetProgressBar);
        }
    }
}
