package com.example.andyapp.queries;

import com.example.andyapp.models.FetchIncome;

import java.util.ArrayList;

public class FetchIncomes {
    ArrayList<FetchIncome> fetchIncomeArrayList;

    public FetchIncomes(ArrayList<FetchIncome> fetchIncomeArrayList) {
        this.fetchIncomeArrayList = fetchIncomeArrayList;
    }
    public ArrayList<FetchIncome> getFetchIncomeArrayList() {
        return fetchIncomeArrayList;
    }
    public void setFetchIncomeArrayList(ArrayList<FetchIncome> fetchIncomeArrayList) {
        this.fetchIncomeArrayList = fetchIncomeArrayList;
    }
}
