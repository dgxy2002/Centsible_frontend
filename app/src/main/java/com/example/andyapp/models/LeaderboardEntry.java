package com.example.andyapp.models;

public class LeaderboardEntry {
    public String userId;
    public String username;
    public int score;

    public LeaderboardEntry(String userId, String username, int score) {
        this.userId = userId;
        this.username = username;
        this.score = score;
    }
}
