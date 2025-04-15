package com.example.andyapp;

public class UsernameModel {
    private String name;
    private String number_points;
    private String pointsUpDown;
    private String imageUrl; // 🔁 from int to String
    private int arrowUpDown_img;
    private String userId;

    public UsernameModel(String name, String number_points, String pointsUpDown, String imageUrl, int arrowUpDown_img, String userId) {
        this.name = name;
        this.number_points = number_points;
        this.pointsUpDown = pointsUpDown;
        this.imageUrl = imageUrl;
        this.arrowUpDown_img = arrowUpDown_img;
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public String getNumber_points() {
        return number_points;
    }

    public String getPointsUpDown() {
        return pointsUpDown;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public int getArrowUpDown_img() {
        return arrowUpDown_img;
    }

    public String getUserId() {
        return userId;
    }
}
