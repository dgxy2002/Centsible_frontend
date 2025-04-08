package com.example.andyapp.models;

public class BudgetModel {
    private double spent;
    private double budget;
    private String category;
    private int image;
    public BudgetModel(int image, double spent, double budget, String category) {
        this.image = image;
        this.spent = spent;
        this.category = category;
        this.budget = budget;
    }

    public double getBudget() {return budget;}
    public double getSpent() {
        return spent;
    }

    public String getCategory() {
        return category;
    }
    public int getImage() {
        return image;
    }
}
