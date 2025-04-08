package com.example.andyapp.models;

import java.util.ArrayList;

public class BudgetModels {
    private ArrayList<BudgetModel> budgetModels;
    private double totalBudget;
    private double totalSpent;

    public BudgetModels() {
        this.budgetModels = new ArrayList<>();
        this.totalBudget = 0;
        this.totalSpent = 0;
    }

    public double getTotalBudget() {
        return totalBudget;
    }

    public double getTotalSpent() {
        return totalSpent;
    }

    public ArrayList<BudgetModel> getBudgetModels() {
        return budgetModels;
    }

    public void setBudgetModels(ArrayList<BudgetModel> budgetModels) {
        this.budgetModels = budgetModels;
        calculateTotalBudget();
        calculateTotalSpent();
    }

    public void addBudgetModel(BudgetModel model){
        budgetModels.add(model);
        totalBudget += model.getBudget();
        totalSpent += model.getSpent();
    }

    private void calculateTotalBudget(){
        this.totalBudget = 0;
        for (BudgetModel model: budgetModels){
            totalBudget += model.getBudget();
        }
    }

    private void calculateTotalSpent(){
        this.totalSpent = 0;
        for (BudgetModel model: budgetModels){
            totalSpent += model.getBudget();
        }
    }
}
