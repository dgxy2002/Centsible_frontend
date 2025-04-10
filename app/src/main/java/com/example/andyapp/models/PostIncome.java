package com.example.andyapp.models;

public class PostIncome extends TransanctionModel{
    protected String userId;
    public PostIncome(String title, double amount, String createdDate, String userId) {
        super(title, amount, createdDate);
        this.userId = userId;
    }

    public String getUserId() {
        return userId;
    }
}
