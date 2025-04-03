package com.example.andyapp.queries.mongoModels;

import androidx.annotation.NonNull;

import java.time.LocalDate;

public class PostExpense {
    private String title;
    private double amount;
    private String category;
    private LocalDate createdDate; // Must match "createdDate" in backend
    private String userId;

    public PostExpense(String userId, String title, double amount, String category, LocalDate createdDate) {
        this.userId = userId;
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.createdDate = createdDate;
    }
    @Override @NonNull
    public String toString(){
        String out_string = String.format("title: %s, amount: %.2f, userId: %s, category: %s, createdDate: %s", title, amount, userId, category, createdDate.toString());
        return out_string;
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

    public String getUserId() {
        return userId;
    }
}
