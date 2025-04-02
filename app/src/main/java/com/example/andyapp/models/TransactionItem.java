package com.example.andyapp.models;

public class TransactionItem {
    public String amount;
    public String description;
    public String type;

    public String getAmount() {
        return amount;
    }

    public String getDescription() {
        return description;
    }

    public String getType() {
        return type;
    }

    public TransactionItem(String amount, String description, String type) {
        this.amount = amount;
        this.description = description;
        this.type = type;
    }
}