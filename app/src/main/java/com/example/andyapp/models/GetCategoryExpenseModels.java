package com.example.andyapp.models;

import java.util.ArrayList;

public class GetCategoryExpenseModels {
    ArrayList<GetCategoryExpenseModel> categoryExpensesModels;
    public GetCategoryExpenseModels(ArrayList<GetCategoryExpenseModel> categoryExpensesModels) {
        this.categoryExpensesModels = categoryExpensesModels;
    }
    public ArrayList<GetCategoryExpenseModel> getCategoryExpensesModels() {
        return categoryExpensesModels;
    }

    public void setCategoryExpensesModels(ArrayList<GetCategoryExpenseModel> categoryExpensesModels) {
        this.categoryExpensesModels = categoryExpensesModels;
    }
}
