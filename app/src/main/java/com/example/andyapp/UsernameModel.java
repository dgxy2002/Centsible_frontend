package com.example.andyapp;

public class UsernameModel {
    private String name;
    private String number_points;
    private String pointsUpDown;
    private int image;
    private int arrowUpDown_img;
    private String userId;

    public UsernameModel(String name, String number_points, String pointsUpDown, int image, int arrowUpDown_img, String userId) {
        this.name = name;
        this.number_points = number_points;
        this.pointsUpDown = pointsUpDown;
        this.image = image;
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

    public int getImage() {
        return image;
    }

    public int getArrowUpDown_img() {
        return arrowUpDown_img;
    }

    public String getUserId() {
        return userId;
    }
}
