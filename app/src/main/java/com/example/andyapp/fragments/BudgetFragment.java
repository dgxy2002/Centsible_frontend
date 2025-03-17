package com.example.andyapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.example.andyapp.R;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetFragment extends Fragment {
    private int budgetProgress = 25;
    private ProgressBar budgetProgressBar;
    private ArrayList<ProgressBar> progressBarArrayList;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_budget, container, false);
        // Inflate the layout for this fragment
        budgetProgressBar = view.findViewById(R.id.budgetProgressBar);
        budgetProgressBar.setProgress(budgetProgress);
        String[] categories = getResources().getStringArray(R.array.categories);
        float[] budgetData = getBudgetData().get(0);
        float[] spentData = getBudgetData().get(1);
        return view;
    }

    ArrayList<float[]> getBudgetData(){
        //get budget data here
        float[] budgetData = {215F, 315F, 100F, 500F, 600F};
        float[] spentData = {125F, 120F, 50F, 375, 42};
        ArrayList<float[]> data = new ArrayList<>();
        data.add(budgetData);
        data.add(spentData);
        return data;
    }

    void setProgressBars(ArrayList<String> categories, ArrayList<float[]> budgetData){
        for (int i = 0; i < categories.size(); i++){
            float amountSpent = budgetData.get(i)[0];
            float budget = budgetData.get(i)[0];
            String category = categories.get(i);
            int percent = (int) (amountSpent/budget);
            //Conditionally render progress bars

        }
    }
}