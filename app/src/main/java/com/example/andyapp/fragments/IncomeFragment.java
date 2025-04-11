package com.example.andyapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.example.andyapp.DataSubject;
import com.example.andyapp.LineChartObserver;
import com.example.andyapp.LoginActivity;
import com.example.andyapp.NavigationDrawerActivity;
import com.example.andyapp.R;
import com.example.andyapp.VerticalTextView;
import com.example.andyapp.adapters.IncomeRecyclerViewAdapter;
import com.example.andyapp.models.FetchIncome;
import com.example.andyapp.models.GetCategoryExpenseModel;
import com.example.andyapp.queries.FetchIncomes;
import com.example.andyapp.queries.IncomeService;
import com.example.andyapp.utils.SortExpenseByAmount;
import com.example.andyapp.utils.SortExpenseByName;
import com.example.andyapp.utils.SortIncomeByDay;
import com.example.andyapp.utils.SortIncomeByName;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link IncomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class IncomeFragment extends Fragment {
    private LineChart lineChart;
    private LineChartObserver lineChartObserver;
    private RecyclerView recyclerView;
    private FetchIncomes fetchIncomes;
    private IncomeRecyclerViewAdapter adapter;
    private DataSubject<FetchIncomes> subject;
    private IncomeService incomeService;
    private SharedPreferences mPref;
    private FloatingActionButton btnLogIncome;
    private AutoCompleteTextView dropdownSort;
    private String[] sortingTypes;
    private ArrayAdapter<String> sortingAdapter;
    private String userId;
    private String viewerId;
    private String TAG = "LOGCAT";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_income, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Permissions
        mPref = getContext().getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        userId = mPref.getString(LoginActivity.USERKEY, LoginActivity.DEFAULT_USERID);
        viewerId = mPref.getString(LoginActivity.VIEWERKEY, LoginActivity.DEFAULT_USERID);
        //Initialise views and DataSubject and data
        dropdownSort = view.findViewById(R.id.dropdownSortIncome);
        sortingTypes = getContext().getResources().getStringArray(R.array.sorting_income);
        sortingAdapter = new ArrayAdapter<>(requireContext(), R.layout.dropdownitem, sortingTypes);
        dropdownSort.setAdapter(sortingAdapter);
        dropdownSort.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String sortingType = adapterView.getItemAtPosition(i).toString();
                ArrayList<FetchIncome> models = fetchIncomes.getFetchIncomeArrayList();
                switch (sortingType) {
                    case "Latest":
                        models.sort(new SortIncomeByDay());
                        break;
                    case "Oldest":
                        models.sort(new SortIncomeByDay().reversed());
                        break;
                    case "A-Z":
                        models.sort(new SortIncomeByName());
                        break;
                }
                fetchIncomes.setFetchIncomeArrayList(models);
                subject.notifyObservers(fetchIncomes);
            }
        });
        lineChart = view.findViewById(R.id.incomeLineChart);
        lineChartObserver = new LineChartObserver(lineChart, requireContext());
        recyclerView = view.findViewById(R.id.incomeRecycler);
        btnLogIncome = view.findViewById(R.id.btnAddIncome);
        subject = new DataSubject<>();
        fetchIncomes = new FetchIncomes(new ArrayList<>());
        incomeService = new IncomeService(requireContext());
        //Set Line Chart Title

        //Configure btnLogIncome
        if (!userId.equals(viewerId)) {
            btnLogIncome.setVisibility(View.INVISIBLE);
        }
        btnLogIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireContext(), NavigationDrawerActivity.class);
                intent.putExtra(NavigationDrawerActivity.FRAGMENT_TAG, "LogExpense");
                startActivity(intent);
            }
        });

        //Configure RecyclerView
        adapter = new IncomeRecyclerViewAdapter(requireContext(), new ArrayList<>());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        //Register observers
        subject.registerObserver(lineChartObserver);
        subject.registerObserver(adapter);
        setFetchIncomes();
    }

    public void setFetchIncomes(){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Looper looper = Looper.getMainLooper();
        Handler handler = new Handler(looper);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                LocalDate currentDate = LocalDate.now();
                int month = currentDate.getMonthValue();
                int year = currentDate.getYear();
                fetchIncomes = incomeService.fetchIncomesByMonth(viewerId, month, year, subject, handler);
            }
        });
    }
}