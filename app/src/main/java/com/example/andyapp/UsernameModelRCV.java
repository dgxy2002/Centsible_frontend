package com.example.andyapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class UsernameModelRCV extends RecyclerView.Adapter<UsernameModelRCV.MyViewHolder> {
    Context context;
    ArrayList<UsernameModel> usernameModels;

    public UsernameModelRCV(Context context, ArrayList<UsernameModel> usernameModels){
        this.context = context;
        this.usernameModels = usernameModels;

    }


    @NonNull
    @Override
    public UsernameModelRCV.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_row,parent,false);
        return new UsernameModelRCV.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsernameModelRCV.MyViewHolder holder, int position) {
        holder.name.setText(usernameModels.get(position).getName());
        holder.points.setText(usernameModels.get(position).getNumber_points());
        holder.img_arrow.setImageResource(usernameModels.get(position).getArrowUpDown_img());
        holder.img_profile.setImageResource(usernameModels.get(position).getImage());
        holder.pointsUpDown.setText(usernameModels.get(position).getPointsUpDown());

    }

    @Override
    public int getItemCount() {
        return usernameModels.size();
    }
    public static class MyViewHolder extends RecyclerView.ViewHolder{
        // grabbing the views from our recycler view layout
        ImageView img_profile, img_arrow;
        TextView name, points, pointsUpDown;
        public MyViewHolder(@NonNull View itemView){
            super(itemView);

            img_profile = itemView.findViewById(R.id.profilepicture);
            img_arrow = itemView.findViewById(R.id.arrowUpDown);
            name = itemView.findViewById(R.id.username);
            points = itemView.findViewById(R.id.points);
            pointsUpDown = itemView.findViewById(R.id.arrowUpDownText);

        }
    }
}
