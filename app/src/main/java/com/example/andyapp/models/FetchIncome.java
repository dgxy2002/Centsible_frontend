package com.example.andyapp.models;

public class FetchIncome extends TransanctionModel{
    private String IncomeId;

    public FetchIncome(String IncomeId, String title, double amount, String createdDate) {
        super(title, amount, createdDate);
    }

    public String getIncomeId() {
        return IncomeId;
    }
}
