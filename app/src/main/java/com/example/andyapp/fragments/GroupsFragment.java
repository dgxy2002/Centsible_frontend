package com.example.andyapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.andyapp.R;
import com.example.andyapp.adapters.Groups_RecyclerViewAdapter;
import com.example.andyapp.models.GroupsModel;

import java.util.ArrayList;


public class GroupsFragment extends Fragment {
    RecyclerView groupsRecycler;
    ArrayList<GroupsModel> groupsModels;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_groups, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        groupsRecycler = view.findViewById(R.id.groupsRecycler);
        groupsModels = new ArrayList<>();
        setUpGroupsModels();
        Groups_RecyclerViewAdapter adapter = new Groups_RecyclerViewAdapter(groupsModels, view.getContext());
        groupsRecycler.setAdapter(adapter);
        groupsRecycler.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }
    private void setUpGroupsModels(){
        String[] names = {"Herbert", "Robert", "Philbert", "Sherbert"};
        String[] relationship = {"Elderly", "Elderly", "Child", "child"};
        int[] image = {R.drawable.avatar, R.drawable.avatar, R.drawable.avatar, R.drawable.avatar};
        for (int i = 0; i < names.length; i++){
            groupsModels.add(new GroupsModel(relationship[i], names[i], image[i]));
        }
    }
}