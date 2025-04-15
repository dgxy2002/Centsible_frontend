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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.andyapp.DataObserver;
import com.example.andyapp.DataSubject;
import com.example.andyapp.LoginActivity;
import com.example.andyapp.NavigationDrawerActivity;
import com.example.andyapp.PieChartExpenseObserver;
import com.example.andyapp.adapters.ExpRecyclerViewAdapter;
import com.example.andyapp.models.GetCategoryExpenseModel;
import com.example.andyapp.R;
import com.example.andyapp.models.GetCategoryExpenseModels;
import com.example.andyapp.queries.ExpenseService;
import com.example.andyapp.queries.UserService;
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

    int[] colorResIds;


    GetCategoryExpenseModels getCategoryExpenseModels;
    ArrayList<Integer> colors;
    ArrayAdapter<String> sortingAdapter;
    PieChart pieChart;
    PieChartExpenseObserver pieChartObserver;
    RecyclerView recyclerView;
    ImageButton btnAdd;
    ImageView profilePicView;
    private ExpRecyclerViewAdapter adapter;
    AutoCompleteTextView dropdownSorting;
    DataSubject<GetCategoryExpenseModels> subject;
    SharedPreferences myPref;
    String userId;
    String viewerId;
    String token;
    String TAG = "LOGCAT";

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Get Permissions Information
        myPref = requireActivity().getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        userId = myPref.getString(LoginActivity.USERKEY, LoginActivity.DEFAULT_USERID);
        viewerId = myPref.getString(LoginActivity.VIEWERKEY, LoginActivity.DEFAULT_USERID);
        token = myPref.getString(LoginActivity.TOKENKEY, LoginActivity.DEFAULT_USERID);
        //Initialise Variables/Views
        colors = new ArrayList<>();
        colorResIds = requireContext().getResources().getIntArray(R.array.category_colors);
        for (int colorResId : colorResIds) {
            colors.add(colorResId);
        }
        //Initialise Profile Picture
        profilePicView = view.findViewById(R.id.expenseProfilePicture);
        profilePicView.setVisibility(View.INVISIBLE);
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
        adapter = new ExpRecyclerViewAdapter(view.getContext(), getCategoryExpenseModels);
        recyclerView.setAdapter(adapter);
        subject.registerObserver(adapter);
        subject.registerObserver(pieChartObserver);
        subject.registerObserver(new ProfilePictureObserver());
        updateExpenseObservers();
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_expense, container, false);
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

    class ProfilePictureObserver implements DataObserver<GetCategoryExpenseModels>{
        @Override
        public void updateData(GetCategoryExpenseModels data) {
            UserService userService = new UserService(requireContext());
            Looper looper = Looper.getMainLooper();
            Handler handler = new Handler(looper);
            userService.getUserImage(userId, handler, profilePicView);
        }
    }
}