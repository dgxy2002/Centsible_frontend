package com.example.andyapp.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.andyapp.LogBudget;
import com.example.andyapp.R;
import com.example.andyapp.adapters.Bd_RecyclerViewAdapter;
import com.example.andyapp.models.BudgetModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetFragment extends Fragment {
    private int budgetProgress = 25;
    private ProgressBar budgetProgressBar;
    private ArrayList<BudgetModel> budgetModels;
    FloatingActionButton btnEditBudget;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
        // Inflate the layout for this fragment
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnEditBudget = view.findViewById(R.id.btnEditBudget);
        budgetProgressBar = view.findViewById(R.id.budgetProgressBar);
        budgetProgressBar.setProgress(budgetProgress);

        btnEditBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LogBudget.class);
                startActivity(intent);
            }
        });

        int[] images = {R.drawable.dining, R.drawable.dining, R.drawable.dining, R.drawable.dining, R.drawable.dining, R.drawable.dining, R.drawable.dining, R.drawable.dining};
        budgetModels = new ArrayList<>();
        String[] categories = getResources().getStringArray(R.array.categories);
        float[] budgetData = getBudgetData().get(0);
        float[] spentData = getBudgetData().get(1);
        int[] progresses = getBudgetProgress(budgetData, spentData);

        setupBudgetModels(progresses, images, spentData, budgetData, categories);
        RecyclerView bdRecyclerView = view.findViewById(R.id.bdrecyclerView);
        Bd_RecyclerViewAdapter adapter = new Bd_RecyclerViewAdapter(view.getContext(), budgetModels);
        bdRecyclerView.setAdapter(adapter);
        bdRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


    }

    void setupBudgetModels(int[]progresses, int[]images, float[] spentData, float[] budgetData, String[] categories){
        for(int i = 0; i < categories.length; i++){
            budgetModels.add(new BudgetModel(progresses[i], images[i], spentData[i], budgetData[i], categories[i]));
        }
    }

    ArrayList<float[]> getBudgetData(){
        //get budget data here
        float[] budgetData = {215F, 315F, 100F, 500F, 600F, 1000000F, 600F, 800F};
        float[] spentData = {125F, 120F, 50F, 375F, 42F, 600F, 50F, 600F};
        ArrayList<float[]> data = new ArrayList<>();
        data.add(budgetData);
        data.add(spentData);
        return data;
    }

    int[] getBudgetProgress(float[]budgetData, float[] spentData){
        int[] progresses = new int[budgetData.length];
        for (int i = 0; i < budgetData.length; i++){
            float amountSpent = spentData[i];
            float budget = budgetData[i];
            int progress = (int) (amountSpent * 100 /budget);
            progresses[i] = progress;
        }
        return progresses;
    }

}