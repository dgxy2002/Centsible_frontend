package com.example.andyapp.adapters;
import com.example.andyapp.DataObserver;
import com.example.andyapp.LoginActivity;
import com.example.andyapp.NavigationDrawerActivity;
import com.example.andyapp.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.andyapp.RecyclerViewSpacingDecorator;
import com.example.andyapp.models.GroupsModel;
import com.example.andyapp.models.GroupsModels;

import java.util.ArrayList;

public class Groups_RecyclerViewAdapter extends RecyclerView.Adapter<Groups_RecyclerViewAdapter.MyViewHolder> implements DataObserver<GroupsModels> {
    Context context;
    ArrayList<GroupsModel> groupsModels;
    SharedPreferences mPref;

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
        SharedPreferences mPref = context.getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        GroupsModel model = groupsModels.get(position);
        holder.imageView.setImageResource(model.getImage());
        holder.nameView.setText(model.getName());
        holder.relationView.setText("Connection");
        holder.btnNudge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = model.getName();
                Toast.makeText(context, "Name: " + name, Toast.LENGTH_SHORT).show();
            }
        });
        holder.groupsBtnView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = mPref.edit();
                String connectionId = model.getUserId();
                editor.putString(LoginActivity.VIEWERKEY, connectionId);
                editor.apply();
                Intent intent = new Intent(context, NavigationDrawerActivity.class);
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return groupsModels.size();
    }

    @Override
    public void updateData(GroupsModels data) {
        this.groupsModels = data.getGroupsModels();
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView nameView, relationView;
        AppCompatButton btnNudge;
        ImageButton groupsBtnView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.imageView = itemView.findViewById(R.id.avatarView);
            this.nameView = itemView.findViewById(R.id.nameView);
            this.relationView = itemView.findViewById(R.id.relationView);
            this.btnNudge = itemView.findViewById(R.id.btnNudge);
            this.groupsBtnView = itemView.findViewById(R.id.groupsBtnView);
        }
    }
}
