package com.example.andyapp.models;

public class CategoryAllocation {
    private final String id;
    private final String userId;
    private final String category;
    private final double allocatedAmount;
    private final double spentAmount;

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
