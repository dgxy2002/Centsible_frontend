package com.example.andyapp.models;

public class LeaderboardUser {
    private String userId;
    private String username;
    private int score;

    public LeaderboardUser(String userId, String username, int score) {
        this.userId = userId;
        this.username = username;
        this.score = score;
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
}
