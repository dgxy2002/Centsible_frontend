package com.example.andyapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.andyapp.LoginActivity;
import com.example.andyapp.adapters.Exp_RecyclerViewAdapter;
import com.example.andyapp.models.ExpensesModel;
import com.example.andyapp.LogExpense;
import com.example.andyapp.R;
import com.example.andyapp.queries.ExpenseService;
import com.example.andyapp.queries.RetrofitClient;
import com.example.andyapp.queries.mongoModels.Expense;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {

    interface GetExpenses{
        @GET("expenses/user/{userId}")
        Call<ArrayList<Expense>>getExpenses(@Path("userId") String userId);
    }

    int[] colorResIds = {
            R.color.pie1, R.color.pie2, R.color.pie3, R.color.pie4,
            R.color.pie5, R.color.pie6, R.color.pie7, R.color.pie8
    };


    ArrayList<ExpensesModel> expensesModels = new ArrayList<>();
    ArrayList<PieEntry> entries = new ArrayList<>();
    ArrayList<Integer> colors;
    PieChart pieChart;
    RecyclerView recyclerView;
    ImageButton btnAdd;
    private Exp_RecyclerViewAdapter adapter;
    GetExpenses getExpense;
    SharedPreferences myPref;
    String userId;
    String viewerId;
    String token;
    String TAG = "LOGCAT";


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_expense, container, false);
        //Initialise Variables/Views
        colors = new ArrayList<>();
        for (int colorResId : colorResIds) {
            colors.add(ContextCompat.getColor(requireActivity(), colorResId));
        }
        myPref = requireActivity().getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        viewerId = myPref.getString(LoginActivity.VIEWERKEY, "67ecf4e07cb6ed67c0e7e67a");
        token = myPref.getString(LoginActivity.TOKENKEY, "67ecf4e07cb6ed67c0e7e67a");

        pieChart = view.findViewById(R.id.expensePieChart);
        recyclerView = view.findViewById(R.id.expenserecycler);
        btnAdd = view.findViewById(R.id.btnAdd);

        int black = ContextCompat.getColor(requireContext(), R.color.black);
        Typeface font = ResourcesCompat.getFont(requireContext(), R.font.poppins_semibold);
        //Initialise Button Listeners
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), LogExpense.class);
                startActivity(intent);
            }
        });
        //Configure Pie Chart
        PieDataSet pieDataSet = getDataSet();
        formatPieDataSet(pieDataSet, colors, black, font);
        PieData pieData = new PieData(pieDataSet);
        pieChart.setData(pieData);
        formatPieChart(pieChart, black);
        //Configure RecyclerView
        adapter = new Exp_RecyclerViewAdapter(view.getContext(), expensesModels);
        recyclerView.setAdapter(adapter);
        setupExpensesModel();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }

    private void setupExpensesModel(){
        getExpense = RetrofitClient.getRetrofit().create(GetExpenses.class);
        if (viewerId.isEmpty()){
            viewerId = userId;
        }
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(new Runnable() {
            @Override
            public void run() {
                getExpense.getExpenses("67ecf4e07cb6ed67c0e7e67a").enqueue(new Callback<>() {
                    @Override
                    public void onResponse(Call<ArrayList<Expense>> call, Response<ArrayList<Expense>> response) {
                        if (response.body() != null) {
                            Log.d(TAG, response.body().toString());
                        }else{
                            try {
                                String error = response.errorBody().string();
                                Log.e(TAG, "Response code: " + response.code());
                                Log.e(TAG, "Error body: " + error);
                            } catch (IOException e) {
                                Log.e(TAG, "Error reading errorBody", e);
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<ArrayList<Expense>> call, Throwable t) {
                        if (t.getMessage() != null) {
                            Log.d(TAG, t.getMessage());
                        }
                    }
                });
            }
        });
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