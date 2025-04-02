package com.example.andyapp.queries.mongoModels;

import androidx.annotation.NonNull;

import java.time.LocalDate;


public class Expense {
    private String title;
    private double amount;
    private String expenseId;
    private String category;
    private LocalDate createdDate; // Must match "createdDate" in backend

    public Expense(String title, double amount, String expenseId, String category, LocalDate createdDate) {
        this.title = title;
        this.amount = amount;
        this.expenseId = expenseId;
        this.category = category;
        this.createdDate = createdDate;
    }
    @Override @NonNull
    public String toString(){
        String out_string = String.format("title: %s, amount: %.2f, userId: %s, category: %s, date: %s", title, amount, expenseId, category, createdDate.toString());
        return out_string;
    }
    public String getUserID() {
        return expenseId;
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
