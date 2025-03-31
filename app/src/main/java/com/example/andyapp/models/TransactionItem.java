package com.example.andyapp.models;

public class TransactionItem {
    public String amount;
    public String description;
    public String type;

    public TransactionItem(String amount, String description, String type) {
        this.amount = amount;
        this.description = description;
        this.type = type;
    }
}