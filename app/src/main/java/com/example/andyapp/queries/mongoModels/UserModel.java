package com.example.andyapp.queries.mongoModels;

import java.time.LocalDate;

public class UserModel {
    private String id;
    private String lastNudge;

    private String lastLog;
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

    public String getLastNudge() {
        return lastNudge;
    }

    public String getLastLog() {
        return lastLog;
    }
}
