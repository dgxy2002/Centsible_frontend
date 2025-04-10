package com.example.andyapp.models;

public class GetCategoryExpenseModel {
    private String category;
    private double amount;
    private int image;

    public GetCategoryExpenseModel(String category, double amount, int image) {
        this.category = category;
        this.amount = amount;
        this.image = image;
    }

    public int getImage() {
        return image;
    }

    public String getCategory() {
        return category;
    }

    public double getAmount() {
        return amount;
    }

}
