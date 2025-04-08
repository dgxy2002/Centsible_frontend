package com.example.andyapp.models;

public class CategoryAllocation {
    private String id;
    private String userId;
    private String category;
    private double allocatedAmount;
    private double spentAmount;

    public CategoryAllocation(String id, String userId, String category, double allocatedAmount, double spentAmount) {
        this.id = id;
        this.userId = userId;
        this.category = category;
        this.allocatedAmount = allocatedAmount;
        this.spentAmount = spentAmount;
    }
    public String getId() {
        return id;
    }

    public String getUserId() {
        return userId;
    }

    public String getCategory() {
        return category;
    }

    public double getAllocatedAmount() {
        return allocatedAmount;
    }

    public double getSpentAmount() {
        return spentAmount;
    }
}
