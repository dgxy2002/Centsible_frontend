package com.example.andyapp.adapters;
import com.example.andyapp.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.RecyclerViewSpacingDecorator;
import com.example.andyapp.models.GroupsModel;

import java.util.ArrayList;

public class Groups_RecyclerViewAdapter extends RecyclerView.Adapter<Groups_RecyclerViewAdapter.MyViewHolder>{
    Context context;
    ArrayList<GroupsModel> groupsModels;

    public Groups_RecyclerViewAdapter(ArrayList<GroupsModel> groupsModels, Context context) {
        this.groupsModels = groupsModels;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.groups_recycler_view_row, parent, false);
        return new Groups_RecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.imageView.setImageResource(groupsModels.get(position).getImage());
        holder.nameView.setText(groupsModels.get(position).getName());
        holder.relationView.setText("Connection");
        holder.btnNudge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = groupsModels.get(position).getName();
                Toast.makeText(context, "Name: " + name, Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupsModels.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView nameView, relationView;
        AppCompatButton btnNudge;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.avatarView);
            this.nameView = itemView.findViewById(R.id.nameView);
            this.relationView = itemView.findViewById(R.id.relationView);
            this.btnNudge = itemView.findViewById(R.id.btnNudge);
        }
    }
}
