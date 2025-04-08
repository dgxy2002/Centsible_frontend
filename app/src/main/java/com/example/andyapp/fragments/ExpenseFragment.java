package com.example.andyapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;

import com.example.andyapp.DataSubject;
import com.example.andyapp.LoginActivity;
import com.example.andyapp.PieChartExpenseObserver;
import com.example.andyapp.adapters.Exp_RecyclerViewAdapter;
import com.example.andyapp.models.GetCategoryExpenseModel;
import com.example.andyapp.LogExpense;
import com.example.andyapp.R;
import com.example.andyapp.models.GetCategoryExpenseModels;
import com.example.andyapp.queries.ExpenseService;
import com.example.andyapp.utils.SortExpenseByAmount;
import com.example.andyapp.utils.SortExpenseByName;
import com.github.mikephil.charting.charts.PieChart;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ExpenseFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ExpenseFragment extends Fragment {

    interface GetExpenses{
        @GET("expenses/user/{userId}/total-by-category")
        Call<HashMap<String, Double>>getExpenses(@Path("userId") String userId);
    }

    int[] colorResIds = {
            R.color.pie1, R.color.pie2, R.color.pie3, R.color.pie4,
            R.color.pie5, R.color.pie6, R.color.pie7, R.color.pie8
    };


    GetCategoryExpenseModels getCategoryExpenseModels;
    ArrayList<Integer> colors;
    ArrayAdapter<String> sortingAdapter;
    PieChart pieChart;
    PieChartExpenseObserver pieChartObserver;
    RecyclerView recyclerView;
    ImageButton btnAdd;
    private Exp_RecyclerViewAdapter adapter;
    AutoCompleteTextView dropdownSorting;
    DataSubject<GetCategoryExpenseModels> subject;
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
        //Initialise dropdown menu
        String[] sortingTypes = requireActivity().getResources().getStringArray(R.array.sorting_types);
        sortingAdapter = new ArrayAdapter<>(requireActivity(), R.layout.dropdownitem, sortingTypes);
        dropdownSorting = view.findViewById(R.id.dropdownSort);
        dropdownSorting.setAdapter(sortingAdapter);
        dropdownSorting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sortingType = adapterView.getItemAtPosition(i).toString();
                ArrayList<GetCategoryExpenseModel> models = getCategoryExpenseModels.getCategoryExpensesModels();
                switch (sortingType) {
                    case "Increasing":
                        models.sort(new SortExpenseByAmount());
                        break;
                    case "Decreasing":
                        models.sort(new SortExpenseByAmount().reversed());
                        break;
                    case "A-Z":
                        models.sort(new SortExpenseByName());
                        break;
                }
                getCategoryExpenseModels.setCategoryExpensesModels(models);
                subject.notifyObservers(getCategoryExpenseModels);
            }
        });
        //Get Permissions Information
        myPref = requireActivity().getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        viewerId = myPref.getString(LoginActivity.VIEWERKEY, LoginActivity.DEFAULT_USERID);
        token = myPref.getString(LoginActivity.TOKENKEY, LoginActivity.DEFAULT_USERID);

        getCategoryExpenseModels = new GetCategoryExpenseModels(new ArrayList<GetCategoryExpenseModel>());
        subject = new DataSubject<GetCategoryExpenseModels>();
        pieChart = view.findViewById(R.id.expensePieChart);
        pieChartObserver = new PieChartExpenseObserver(pieChart, requireContext());
        recyclerView = view.findViewById(R.id.expenserecycler);
        btnAdd = view.findViewById(R.id.btnAdd);
        //Initialise Button Listeners
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), LogExpense.class);
                startActivity(intent);
            }
        });
        //Configure Pie Chart
//        PieDataSet pieDataSet = getDataSet();
//        formatPieDataSet(pieDataSet, colors, black, font);
//        PieData pieData = new PieData(pieDataSet);
//        pieChart.setData(pieData);
//        formatPieChart(pieChart, black);
        //Configure RecyclerView
        adapter = new Exp_RecyclerViewAdapter(view.getContext(), getCategoryExpenseModels);
        recyclerView.setAdapter(adapter);
        subject.registerObserver(adapter);
        subject.registerObserver(pieChartObserver);
        updateExpenseObservers();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }

    private void updateExpenseObservers(){
        Log.d(TAG, "USERID" + viewerId);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Looper looper = Looper.getMainLooper();
        final Handler handler = new Handler(looper);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ExpenseService expenseService = new ExpenseService(requireContext());
                getCategoryExpenseModels =  expenseService.fetchTotalExpensesByCategory(viewerId, handler, subject);
            }
        });
    }
//    private PieDataSet getDataSet(){
//        //add code to get Data Later
//        entries.add(new PieEntry(800f, "Shopping"));
//        entries.add(new PieEntry(1200f, "Transport"));
//        entries.add(new PieEntry(600f, "Food"));
//        entries.add(new PieEntry(700f, "Education"));
//        return new PieDataSet(entries, "Expenses");
//    }
//    private void formatPieDataSet(PieDataSet pieDataSet, ArrayList<Integer> colors, int black, Typeface font){
//        pieDataSet.setSliceSpace(3f);
//        pieDataSet.setColors(colors);
//        pieDataSet.setValueTypeface(font);
//        pieDataSet.setXValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        pieDataSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);
//        pieDataSet.setValueLinePart1OffsetPercentage(100f); // Offset of the first part of the line
//        pieDataSet.setValueLinePart1Length(0.4f);           // Length of the first part of the line
//        pieDataSet.setValueLinePart2Length(0.6f);           // Length of the second part of the line
//        pieDataSet.setValueLineColor(black);
//        pieDataSet.setValueTextSize(12f);     // Size of the value text
//        pieDataSet.setValueTextColor(black); // Color of the value text
//    }
//    private void formatPieChart(PieChart pieChart, int black){
//        pieChart.setEntryLabelColor(black);
//        pieChart.setExtraOffsets(20, 20, 20, 20);
//        pieChart.setNoDataText("Log Your Expenses");
//        pieChart.getDescription().setEnabled(false);
//        pieChart.getLegend().setEnabled(false);
//        pieChart.setHoleRadius(40f);
//        pieChart.animateY(1000);
//        pieChart.invalidate();
//    }

}