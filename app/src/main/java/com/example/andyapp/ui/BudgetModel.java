package com.example.andyapp.ui;

import java.util.ArrayList;

public class BudgetModel {

    private float[] budgetData;
    private float[] spentData;
    private String[] categories;

    public BudgetModel(float[] budgetData, float[] spentData, String[] categories) {
        this.budgetData = budgetData;
        this.spentData = spentData;
        this.categories = categories;
    }

    public float[] getBudgetData() {
        return budgetData;
    }

    public float[] getSpentData() {
        return spentData;
    }

    public String[] getCategories() {
        return categories;
    }

}
