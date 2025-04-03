package com.example.andyapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.R;
import com.example.andyapp.RecyclerViewOnClickInterface;
import com.example.andyapp.models.LogBudgetModel;

import java.util.ArrayList;


public class LogBd_RecyclerViewAdapter extends RecyclerView.Adapter<LogBd_RecyclerViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<LogBudgetModel> logBudgetModels;
    private final RecyclerViewOnClickInterface recyclerViewInterface;
    String TAG = "LOGCAT";


    public LogBd_RecyclerViewAdapter(Context context, ArrayList<LogBudgetModel> logBudgetModels, RecyclerViewOnClickInterface recyclerViewInterface) {
        this.context = context;
        this.logBudgetModels = logBudgetModels;
        this.recyclerViewInterface = recyclerViewInterface;
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
        LogBudgetModel logBudgetModel = logBudgetModels.get(position);
        String category = logBudgetModel.getCategory();
        int icon = logBudgetModel.getIcon();
        String budget = logBudgetModel.getBudget();
        int id = logBudgetModel.getId();
        //Initialise Views
        holder.imageView.setImageResource(icon);
        holder.categoryTextView.setText(category);
        holder.budgetTextView.setText("$" + budget);
    }

    @Override
    public int getItemCount() {
        return logBudgetModels.size();
    }

    private void updateData(){

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
