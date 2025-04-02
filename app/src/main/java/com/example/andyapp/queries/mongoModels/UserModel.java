package com.example.andyapp.queries.mongoModels;

import java.util.ArrayList;

public class UserModel {
    private int streak;
    private String token;
    private String id;
    private String message;
    private double budget;
    private ArrayList<String> connections;

    public UserModel(int streak, String token, String id, String message, double budget, ArrayList<String> connections) {
        this.streak = streak;
        this.token = token;
        this.id = id;
        this.budget = budget;
        this.connections = connections;
        this.message = message;
    }

    public int getStreak() {
        return streak;
    }

    public String getToken() {
        return token;
    }

    public String getMessage() {
        return message;
    }

    public String getId() {
        return id;
    }

    public double getBudget() {
        return budget;
    }

    public ArrayList<String> getConnections() {
        return connections;
    }

}

