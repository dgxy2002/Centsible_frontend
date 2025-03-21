package com.example.andyapp.fragments;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.andyapp.adapters.Exp_RecyclerViewAdapter;
import com.example.andyapp.models.ExpensesModel;
import com.example.andyapp.LogExpense;
import com.example.andyapp.R;
import com.example.andyapp.queries.ExpenseService;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.Map;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {
    int[] colorResIds = {
            R.color.pie1, R.color.pie2, R.color.pie3, R.color.pie4,
            R.color.pie5, R.color.pie6, R.color.pie7, R.color.pie8
    };


    ArrayList<ExpensesModel> expensesModels = new ArrayList<>();
    ArrayList<PieEntry> entries = new ArrayList<>();
    ImageButton btnAdd;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        PieChart pieChart = view.findViewById(R.id.expensePieChart);

        ArrayList<Integer> colors = new ArrayList<>();
        for (int colorResId : colorResIds) {
            colors.add(ContextCompat.getColor(requireActivity(), colorResId));
        }

        int black = ContextCompat.getColor(requireContext(), R.color.black);
        Typeface font = ResourcesCompat.getFont(requireContext(), R.font.poppins_semibold);
        btnAdd = view.findViewById(R.id.btnAdd);
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), LogExpense.class);
                startActivity(intent);
            }
        });
        PieDataSet pieDataSet = getDataSet();
        formatPieDataSet(pieDataSet, colors, black, font);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        formatPieChart(pieChart, black);

        RecyclerView recyclerView = view.findViewById(R.id.expenserecycler);
        setupExpensesModel();
        Exp_RecyclerViewAdapter adapter = new Exp_RecyclerViewAdapter(view.getContext(), expensesModels);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));


        return view;
    }

    private void setupExpensesModel(){
        ExpenseService expenseService = new ExpenseService(this.getContext());
        String userId = "67d3d20a29d0cd06ab44add8"; // Replace with actual userId
        expenseService.fetchTotalExpensesByCategory(userId, new ExpenseService.ExpenseCallback() {
            @Override
            public void onSuccess(Map<String, Double> categoryExpenses) {
                for (Map.Entry<String, Double> entry : categoryExpenses.entrySet()) {
                    Log.d("EXPENSE_DATA", "Category: " + entry.getKey() + " - Total: " + entry.getValue());
                }
            }
            @Override
            public void onError(String errorMessage) {
                Toast.makeText(getContext(), "Failed to fetch", Toast.LENGTH_SHORT).show();
                Log.e("API_ERROR", errorMessage);
            }
        });
        String[] categoryNames = {"Dining", "Shopping", "Transport", "Education"};
        float[] amounts = {150.50F, 137.70F, 250.50F, 1000.00F};
        int[] iconImages = {R.drawable.dining, R.drawable.dining, R.drawable.dining, R.drawable.dining};
        for (int i =0; i < categoryNames.length; i++){
            expensesModels.add(new ExpensesModel(categoryNames[i], amounts[i], iconImages[i]));
        }
    }

    private PieDataSet getDataSet(){
        //add code to get Data Later
        entries.add(new PieEntry(800f, "Shopping"));
        entries.add(new PieEntry(1200f, "Transport"));
        entries.add(new PieEntry(600f, "Food"));
        entries.add(new PieEntry(700f, "Education"));
        return new PieDataSet(entries, "Expenses");
    }
    private void formatPieDataSet(PieDataSet pieDataSet, ArrayList<Integer> colors, int black, Typeface font){
        pieDataSet.setSliceSpace(3f);
        pieDataSet.setColors(colors);
        pieDataSet.setValueTypeface(font);
        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
        pieDataSet.setValueLinePart1OffsetPercentage(100f); // Offset of the first part of the line
        pieDataSet.setValueLinePart1Length(0.4f);           // Length of the first part of the line
        pieDataSet.setValueLinePart2Length(0.6f);           // Length of the second part of the line
        pieDataSet.setValueLineColor(black);
        pieDataSet.setValueTextSize(12f);     // Size of the value text
        pieDataSet.setValueTextColor(black); // Color of the value text
    }
    private void formatPieChart(PieChart pieChart, int black){
        pieChart.setEntryLabelColor(black);
        pieChart.setExtraOffsets(20, 20, 20, 20);
        pieChart.setNoDataText("Log Your Expenses");
        pieChart.getDescription().setEnabled(false);
        pieChart.getLegend().setEnabled(false);
        pieChart.setHoleRadius(40f);
        pieChart.animateY(1000);
        pieChart.invalidate();
    }
}