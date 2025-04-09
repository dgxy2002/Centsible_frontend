package com.example.andyapp.models;

public class TransanctionModel {

    protected String title;
    protected double amount;
    protected String createdDate;

    public TransanctionModel(String title, double amount, String createdDate) {
        this.title = title;
        this.amount = amount;
        this.createdDate = createdDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }
}
