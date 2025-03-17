package com.example.andyapp;

public class ExpensesModel {
    private String category;
    private float amount;
    private int image;

    public ExpensesModel(String category, float amount, int image) {
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

    public float getAmount() {
        return amount;
    }



}
