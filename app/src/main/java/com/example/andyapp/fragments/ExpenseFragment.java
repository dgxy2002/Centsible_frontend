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
import com.example.andyapp.NavigationDrawerActivity;
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
        userId = myPref.getString(LoginActivity.USERKEY, LoginActivity.DEFAULT_USERID);
        viewerId = myPref.getString(LoginActivity.VIEWERKEY, LoginActivity.DEFAULT_USERID);
        token = myPref.getString(LoginActivity.TOKENKEY, LoginActivity.DEFAULT_USERID);

        getCategoryExpenseModels = new GetCategoryExpenseModels(new ArrayList<GetCategoryExpenseModel>());
        subject = new DataSubject<GetCategoryExpenseModels>();
        pieChart = view.findViewById(R.id.expensePieChart);
        pieChartObserver = new PieChartExpenseObserver(pieChart, requireContext());
        recyclerView = view.findViewById(R.id.expenserecycler);
        btnAdd = view.findViewById(R.id.btnAdd);
        //Set BtnAdd Visbility
        if (!userId.equals(viewerId)) {
            btnAdd.setVisibility(View.INVISIBLE);
        }
        //Initialise Button Listeners
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(requireActivity(), NavigationDrawerActivity.class);
                intent.putExtra(NavigationDrawerActivity.FRAGMENT_TAG, "LogExpense");
                startActivity(intent);
            }
        });
        //Configure RecyclerView
        adapter = new Exp_RecyclerViewAdapter(view.getContext(), getCategoryExpenseModels);
        recyclerView.setAdapter(adapter);
        subject.registerObserver(adapter);
        subject.registerObserver(pieChartObserver);
        updateExpenseObservers();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        return view;
    }

    private void updateExpenseObservers() {
        Log.d(TAG, "USERID" + viewerId);
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Looper looper = Looper.getMainLooper();
        final Handler handler = new Handler(looper);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                ExpenseService expenseService = new ExpenseService(requireContext());
                getCategoryExpenseModels = expenseService.fetchTotalExpensesByCategory(viewerId, handler, subject);
            }
        });
    }
}