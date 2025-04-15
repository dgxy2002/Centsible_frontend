package com.example.andyapp.queries.mongoModels;

public class UserModel {
    private String id;
    private String username;
    private String imageUrl;
    private int loginStreak;

    public String getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getLoginStreak() {
        return loginStreak;
    }
}
