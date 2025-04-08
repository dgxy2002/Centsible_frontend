package com.example.andyapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.andyapp.DataObserver;
import com.example.andyapp.DataSubject;
import com.example.andyapp.LogBudget;
import com.example.andyapp.LoginActivity;
import com.example.andyapp.R;
import com.example.andyapp.adapters.Bd_RecyclerViewAdapter;
import com.example.andyapp.models.BudgetModel;
import com.example.andyapp.models.BudgetModels;
import com.example.andyapp.queries.BudgetService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link BudgetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BudgetFragment extends Fragment {
    private int budgetProgress = 25;

    private ArrayList<BudgetModel> budgetModels;
    private FloatingActionButton btnEditBudget;
    private TextView overallBudgetTextView;
    private ProgressBar budgetProgressBar;
    private DataSubject<BudgetModels> subject;
    private BudgetService budgetService;
    private String userId;
    private String token;
    private String username;
    private SharedPreferences mPref;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_budget, container, false);
        // Inflate the layout for this fragment
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //SharedPreferences Permissions
        mPref = requireActivity().getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        userId = mPref.getString(LoginActivity.USERKEY, LoginActivity.DEFAULT_USERID);
        token = mPref.getString(LoginActivity.TOKENKEY, LoginActivity.DEFAULT_USERID);
        subject = new DataSubject<>();
        btnEditBudget = view.findViewById(R.id.btnEditBudget);
        budgetProgressBar = view.findViewById(R.id.budgetProgressBar);
        budgetProgressBar.setProgress(budgetProgress);
        overallBudgetTextView = view.findViewById(R.id.textView5);
        btnEditBudget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), LogBudget.class);
                startActivity(intent);
            }
        });
        budgetModels = new ArrayList<>();
        budgetService = new BudgetService(requireContext());
        RecyclerView bdRecyclerView = view.findViewById(R.id.bdrecyclerView);
        Bd_RecyclerViewAdapter adapter = new Bd_RecyclerViewAdapter(view.getContext(), budgetModels);
        bdRecyclerView.setAdapter(adapter);
        bdRecyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        //Register Observers
        subject.registerObserver(adapter);
        subject.registerObserver(new DataObserver<BudgetModels>() {
            @Override
            public void updateData(BudgetModels data) {
                double totalBudget = data.getTotalBudget();
                String formattedBudget = String.format("%.2f", totalBudget);
                overallBudgetTextView.setText(formattedBudget);
            }
        });

        subject.registerObserver(new DataObserver<BudgetModels>() {
            @Override
            public void updateData(BudgetModels data) {
                double totalBudget = data.getTotalBudget();
                double totalSpent = data.getTotalSpent();
                budgetProgressBar.setProgress((int) (totalSpent/totalBudget * 100));
            }
        });
        setupBudgetModels();
    }
    void setupBudgetModels(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Looper looper = Looper.getMainLooper();
        Handler handler = new Handler(looper);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                budgetService.getBudgetCategories(userId, handler, subject);
            }
        });
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