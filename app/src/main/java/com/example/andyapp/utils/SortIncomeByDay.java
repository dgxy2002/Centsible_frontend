package com.example.andyapp.utils;

import com.example.andyapp.models.FetchIncome;

import java.util.Comparator;

public class SortIncomeByDay implements Comparator<FetchIncome> {
    @Override
    public int compare(FetchIncome income1, FetchIncome income2) {
        int day1 = Integer.parseInt(income1.getCreatedDate().substring(8, 10));
        int day2 = Integer.parseInt(income2.getCreatedDate().substring(8, 10));
        return Integer.compare(day1, day2);
    }
}
