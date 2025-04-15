package com.example.andyapp.models;
public class GroupsModel {
    String name;
    String userId;
    String imageUrl ;
    public GroupsModel(String name, String userId, String imageUrl) {
        this.name = name;
        this.userId = userId;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }
    public String getUserId() {
        return userId;
    }
    public String getImageUrl() {
        return imageUrl;
    }
}
