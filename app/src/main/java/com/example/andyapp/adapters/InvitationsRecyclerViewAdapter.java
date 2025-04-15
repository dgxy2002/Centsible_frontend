package com.example.andyapp.adapters;
import com.bumptech.glide.Glide;
import com.example.andyapp.DataObserver;
import com.example.andyapp.LoginActivity;
import com.example.andyapp.NavigationDrawerActivity;
import com.example.andyapp.R;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
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
import com.example.andyapp.models.InvitationModel;
import com.example.andyapp.models.InvitationModels;
import com.example.andyapp.queries.ApiService;
import com.example.andyapp.queries.BudgetService;
import com.example.andyapp.queries.InvitationService;

import java.util.ArrayList;

public class InvitationsRecyclerViewAdapter extends RecyclerView.Adapter<InvitationsRecyclerViewAdapter.MyViewHolder> implements DataObserver<InvitationModels>{
    Context context;
    ArrayList<InvitationModel> invitationModels;
    SharedPreferences mPref;
    private InvitationService invitationService;
    private String inviteeUsername;
    private String TAG = "LOGCAT";

    public InvitationsRecyclerViewAdapter(ArrayList<InvitationModel> invitationModels, Context context, String inviteeUsername) {
        this.invitationModels = invitationModels;
        this.context = context;
        this.inviteeUsername = inviteeUsername;
        this.invitationService = new InvitationService(context);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.invitations_recycler_view_row, parent, false);
        return new InvitationsRecyclerViewAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        InvitationModel model = invitationModels.get(position);
        Glide.with(context).load(model.getImage()).circleCrop().into(holder.avatarView);
        Log.d(TAG, model.getImage());
        holder.avatarView.setVisibility(View.VISIBLE);
        holder.nameView.setText(model.getInviterName());
        holder.btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inviterUsername = model.getInviterName();
                invitationService.respondToInvitation(inviteeUsername, inviterUsername, true);
                invitationModels.remove(model);
                notifyItemRemoved(position);
            }
        });
        holder.btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inviterId = model.getInviterId();
                invitationService.respondToInvitation(inviteeUsername, inviterId, false);
                invitationModels.remove(model);
                notifyItemRemoved(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return invitationModels.size();
    }

    @Override
    public void updateData(InvitationModels data) {
        this.invitationModels = data.getInvitationModels();
        notifyDataSetChanged();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder{
        private ImageView avatarView;
        private TextView nameView;
        private AppCompatButton btnAccept;
        private AppCompatButton btnDelete;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            avatarView = itemView.findViewById(R.id.inviteAvatarView);
            nameView = itemView.findViewById(R.id.inviteNameView);
            btnAccept = itemView.findViewById(R.id.btnConfirmInvite);
            btnDelete = itemView.findViewById(R.id.btnDeleteInvite);
        }
    }
}

