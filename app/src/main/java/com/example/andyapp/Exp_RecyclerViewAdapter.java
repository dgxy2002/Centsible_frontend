package com.example.andyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class Exp_RecyclerViewAdapter extends RecyclerView.Adapter<Exp_RecyclerViewAdapter.MyViewHolder> {
    Context context;
    ArrayList<ExpensesModel> expensesModels;
    public Exp_RecyclerViewAdapter(Context context, ArrayList<ExpensesModel> expensesModels){
        this.context = context;
        this.expensesModels = expensesModels;
    }
    @NonNull
    @Override
    public Exp_RecyclerViewAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Inflate layout and give look to rows
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recyler_view_row, parent, false);
        return new Exp_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Exp_RecyclerViewAdapter.MyViewHolder holder, int position) {
        //assigning values to each view we create in recycler_row_view file
        //based on the position of the recycler view
        holder.catView.setText(expensesModels.get(position).getCategory());
        holder.amtView.setText(String.valueOf(expensesModels.get(position).getAmount()));
        holder.imageView.setImageResource(expensesModels.get(position).getImage());
    }

    @Override
    public int getItemCount() {
        //Number of total items
        return expensesModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        //grabbing views from recycler_row_view.xml file
        ImageView imageView;
        TextView catView, amtView;
        View roundedBarView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.iconView);
            catView = itemView.findViewById(R.id.categoryView);
            amtView = itemView.findViewById(R.id.amtView);
            roundedBarView = itemView.findViewById(R.id.roundedBarView);
        }
    }
}
