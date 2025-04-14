package com.example.andyapp.models;

import java.time.LocalDate;

public class UserSettings {
    private String username;
    private String imageUrl;
    private String firstname;
    private String lastname;
    private String birthdate;
    private String biography;

    public UserSettings(String username, String imageUrl, String firstname, String lastname, String birthdate, String biography) {
        this.username = username;
        this.imageUrl = imageUrl;
        this.firstname = firstname;
        this.lastname = lastname;
        this.birthdate = birthdate;
        this.biography = biography;
    }

    public UserSettings(){}


    public String getUsername() {
        return username;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public String getBiography() {
        return biography;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public void setBiography(String biography) {
        this.biography = biography;
    }
}
