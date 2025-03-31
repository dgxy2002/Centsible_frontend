package com.example.andyapp.queries.mongoModels;

public class Budget {
    private double budget;
    private String userId;
    private String category;
    private String createdDate; // Must match "createdDate" in backend

    public Budget(double budget, String userId, String category, String createdDate) {
        this.userId = userId;
        this.budget = budget;
        this.category = category;
        this.createdDate = createdDate;
    }

    public String getUserID() {
        return userId;
    }

    public String getCategory() {
        return category;
    }

    public double getBudget() {
        return budget;
    }

    public String getCreatedDate() {
        return createdDate;
    }
}