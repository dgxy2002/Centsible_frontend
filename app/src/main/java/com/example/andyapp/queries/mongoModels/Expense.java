package com.example.andyapp.queries.mongoModels;

import java.time.LocalDate;


public class Expense {
    private String title;
    private double amount;
    private String userId;
    private String category;
    private String createdDate; // Must match "createdDate" in backend

    public Expense(String title, double amount, String userId, String category, String createdDate) {
        this.title = title;
        this.amount = amount;
        this.userId = userId;
        this.category = category;
        this.createdDate = createdDate;
    }

    public String getUserID() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }
}
