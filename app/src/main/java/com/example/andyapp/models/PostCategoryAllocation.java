package com.example.andyapp.models;

public class PostCategoryAllocation {
    private String userId;
    private String category;
    private double allocatedAmount;

    public PostCategoryAllocation(String userId, String category, double allocatedAmount) {
        this.userId = userId;
        this.category = category;
        this.allocatedAmount = allocatedAmount;
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
}
