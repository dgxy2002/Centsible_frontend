package com.example.andyapp.adapters;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.DataObserver;
import com.example.andyapp.R;
import com.example.andyapp.models.BudgetModel;
import com.example.andyapp.models.BudgetModels;

import java.util.ArrayList;

public class Bd_RecyclerViewAdapter extends RecyclerView.Adapter<Bd_RecyclerViewAdapter.MyViewHolder> implements DataObserver<BudgetModels> {

    Context context;
    ArrayList<BudgetModel> budgetModels;

    public Bd_RecyclerViewAdapter(Context context, ArrayList<BudgetModel> budgetModels) {
        this.context = context;
        this.budgetModels = budgetModels;
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
        BudgetModel model = budgetModels.get(position);
        holder.imageView.setImageResource(model.getImage());
        holder.catView.setText(model.getCategory());
        holder.amtView.setText(String.format("$%.2f / $%.2f", model.getSpent(), model.getBudget()));
//        holder.progressBar.setMax(100);
//        holder.progressBar.setProgress((int) ((model.getSpent() / model.getBudget()) * 100));
        int[] colors = context.getResources().getIntArray(R.array.category_colors);
        holder.progressBar.setProgressTintList(ColorStateList.valueOf(colors[position % 4]));
        holder.progressBar.setProgressBackgroundTintList(ColorStateList.valueOf(ContextCompat.getColor(context, R.color.progress_bg)));
        ObjectAnimator.ofInt(holder.progressBar, "progress", (int) ((model.getSpent() / model.getBudget()) * 100))
                .setDuration(1000)
                .start();
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

    @Override
    public void updateData(BudgetModels data) {
        this.budgetModels = data.getBudgetModels();
        notifyDataSetChanged();
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
