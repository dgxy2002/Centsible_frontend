package com.example.andyapp.utils;

import android.util.Log;

import com.example.andyapp.models.FetchIncome;

import java.util.Comparator;

public class SortIncomeByName implements Comparator<FetchIncome> {


    @Override
    public int compare(FetchIncome income1, FetchIncome income2) {
        return income1.getTitle().compareToIgnoreCase(income2.getTitle());
    }
}
