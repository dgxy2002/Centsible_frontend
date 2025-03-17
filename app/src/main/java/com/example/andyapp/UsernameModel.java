package com.example.andyapp;

public class UsernameModel {
    String name;
    String number_points;
    String pointsUpDown;
    int image;

    int arrowUpDown_img;




    public UsernameModel(String name, String number_points, String pointsUpDown, int image, int arrowUpDown_img) {
        this.name = name;
        this.number_points = number_points;
        this.image = image;
        this.pointsUpDown = pointsUpDown;
        this.arrowUpDown_img = arrowUpDown_img;
    }

    public String getName() {
        return name;
    }

    public String getNumber_points() {
        return number_points;
    }

    public int getImage() {
        return image;
    }
    public String getPointsUpDown() {
        return pointsUpDown;
    }
    public int getArrowUpDown_img() {
        return arrowUpDown_img;
    }
}
