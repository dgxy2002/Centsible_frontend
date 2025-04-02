package com.example.andyapp.models;
public class GroupsModel {
    String name;
    int image;
    public GroupsModel(String name, int image) {
        this.name = name;
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }
}
