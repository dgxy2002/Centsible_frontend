package com.example.andyapp.models;

public class LogBudgetModel {
    String category;
    int icon;

    public LogBudgetModel(String category, int icon) {
        this.category = category;
        this.icon = icon;
    }

    public String getCategory() {
        return category;
    }

    public int getIcon() {
        return icon;
    }
}
