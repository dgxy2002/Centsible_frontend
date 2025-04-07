package com.example.andyapp;

import android.content.Context;
import android.graphics.Typeface;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.example.andyapp.models.GetCategoryExpenseModel;
import com.example.andyapp.models.GetCategoryExpenseModels;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;

public class PieChartExpenseObserver implements DataObserver<GetCategoryExpenseModels>{
    Context context;
    PieChart pieChart;

    ArrayList<Integer> colors = new ArrayList<>();
    int[] colorResIds = {
            R.color.pie1, R.color.pie2, R.color.pie3, R.color.pie4,
            R.color.pie5, R.color.pie6, R.color.pie7, R.color.pie8
    };

    public PieChartExpenseObserver(PieChart pieChart, Context context) {
        this.pieChart = pieChart;
        this.context = context;
        for (int colorResId : colorResIds) {
            colors.add(ContextCompat.getColor(context, colorResId));
        }
    }

    @Override
    public void updateData(GetCategoryExpenseModels data) {
        ArrayList<GetCategoryExpenseModel> expenseModels = data.getCategoryExpensesModels();
        ArrayList<PieEntry> entries = new ArrayList<>();
        for (GetCategoryExpenseModel entry: expenseModels){
            String label = entry.getCategory();
            double amount = entry.getAmount();
            PieEntry pieEntry = new PieEntry((float)amount, label);
            entries.add(pieEntry);
        }
        PieDataSet pieDataSet = new PieDataSet(entries, "Expenses");
        formatPieDataSet(pieDataSet);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        formatPieChart(pieChart);
    }

    private void formatPieDataSet(PieDataSet pieDataSet){
        int black = ContextCompat.getColor(context, R.color.black);
        Typeface font = ResourcesCompat.getFont(context, R.font.varela_round);
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

    private void formatPieChart(PieChart pieChart){
        int black = ContextCompat.getColor(context, R.color.black);
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
