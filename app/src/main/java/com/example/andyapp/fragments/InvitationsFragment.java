package com.example.andyapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andyapp.DataSubject;
import com.example.andyapp.LoginActivity;
import com.example.andyapp.R;
import com.example.andyapp.adapters.InvitationsRecyclerViewAdapter;
import com.example.andyapp.models.InvitationModel;
import com.example.andyapp.models.InvitationModels;
import com.example.andyapp.queries.InvitationService;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InvitationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InvitationsFragment extends Fragment {
    private RecyclerView recyclerView;
    private InvitationsRecyclerViewAdapter adapter;
    InvitationModels invitationModels;
    DataSubject<InvitationModels> subject;
    SharedPreferences mPref;
    String userId;
    String token;

    InvitationService invitationService;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_invitations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //SharedPreferences permissions
        mPref = getContext().getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        userId = mPref.getString(LoginActivity.USERKEY, LoginActivity.DEFAULT_USERID);
        recyclerView = view.findViewById(R.id.invitationsRecyclerView);
        invitationModels = new InvitationModels();
        subject = new DataSubject<>();
        invitationService = new InvitationService(requireContext());
        adapter = new InvitationsRecyclerViewAdapter(invitationModels.getInvitationModels(), requireContext(), userId);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        subject.registerObserver(adapter);
        setUpInvitationModels();
    }

    public void setUpInvitationModels(){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Looper looper = Looper.getMainLooper();
        Handler handler = new Handler(looper);
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                invitationService.getInvites(userId, handler, subject);
            }
        });

    }
}