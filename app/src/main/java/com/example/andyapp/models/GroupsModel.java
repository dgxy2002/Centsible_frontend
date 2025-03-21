package com.example.andyapp.models;
public class GroupsModel {
    String relationship;
    String name;
    int image;
    public GroupsModel(String relationship, String name, int image) {
        this.relationship = relationship;
        this.name = name;
        this.image = image;
    }

    public String getRelationship() {
        return relationship;
    }

    public String getName() {
        return name;
    }

    public int getImage() {
        return image;
    }
}
