package com.example.andyapp.models;

public class BudgetModel {
    private int progress;
    private float spent;
    private float budget;
    private String category;
    private int image;
    public BudgetModel(int progress, int image, float spent, float budget, String category) {
        this.progress = progress;
        this.image = image;
        this.spent = spent;
        this.category = category;
        this.budget = budget;
    }

    public float getBudget() {return budget;}

    public float getSpent() {
        return spent;
    }

    public String getCategory() {
        return category;
    }

    public int getProgress() {
        return progress;
    }

    public int getImage() {
        return image;
    }
}
