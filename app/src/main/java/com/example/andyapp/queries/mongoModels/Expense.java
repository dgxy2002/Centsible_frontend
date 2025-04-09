package com.example.andyapp.queries.mongoModels;

import androidx.annotation.NonNull;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Expense {
    private String title;
    private double amount;
    private String expenseId;
    private String category;
    private String createdDate; // Now a String (to match backend format)

    public Expense(String title, double amount, String expenseId, String category, String createdDate) {
        this.title = title;
        this.amount = amount;
        this.expenseId = expenseId;
        this.category = category;
        this.createdDate = createdDate;
    }

    @Override
    @NonNull
    public String toString() {
        return String.format("title: %s, amount: %.2f, userId: %s, category: %s, date: %s",
                title, amount, expenseId, category, createdDate);
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

    public String getCreatedDateRaw() {
        return createdDate;
    }

    // ✅ Use this wherever you want a LocalDate
    public LocalDate getParsedCreatedDate() {
        return LocalDate.parse(createdDate.substring(0, 10), DateTimeFormatter.ISO_LOCAL_DATE);
    }
}
