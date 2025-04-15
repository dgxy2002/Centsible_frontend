package com.example.andyapp.models;

public class LeaderboardUser {
    private String userId;
    private String username;
    private int score;
    private String imageUrl;

    public LeaderboardUser(String userId, String username, int score, String imageUrl) {
        this.userId = userId;
        this.username = username;
        this.score = score;
        this.imageUrl = imageUrl;
    }

    public String getUserId() {
        return userId;
    }

    public String getUsername() {
        return username;
    }

    public int getScore() {
        return score;
    }

    public String getImageUrl() {
        return imageUrl;
    }
}
