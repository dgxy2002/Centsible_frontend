package com.example.andyapp.queries.mongoModels;

import androidx.annotation.NonNull;

import java.time.LocalDate;


public class Expense {
    private String title;
    private double amount;
    private String userId;
    private String category;
    private LocalDate createdDate; // Must match "createdDate" in backend

    public Expense(String title, double amount, String userId, String category, LocalDate createdDate) {
        this.title = title;
        this.amount = amount;
        this.userId = userId;
        this.category = category;
        this.createdDate = createdDate;
    }
    @Override @NonNull
    public String toString(){
        String out_string = String.format("title: %s, amount: %.2f, userId: %s, category: %s, date: %s", title, amount, userId, category, createdDate.toString());
        return out_string;
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

    public LocalDate getCreatedDate() {
        return createdDate;
    }
}
