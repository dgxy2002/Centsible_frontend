package com.example.andyapp.models;
public class GroupsModel {
    String name;
    String userId;
    int image;
    public GroupsModel(String name, String userId, int image) {
        this.name = name;
        this.userId = userId;
        this.image = image;
    }

    public String getName() {
        return name;
    }
    public String getUserId() {
        return userId;
    }
    public int getImage() {
        return image;
    }
}
