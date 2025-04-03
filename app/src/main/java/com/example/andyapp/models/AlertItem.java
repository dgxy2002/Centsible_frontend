package com.example.andyapp.models;

public class AlertItem {
    public String title;
    public String subtitle;
    public String type;
    public boolean isRead;

    public AlertItem(String title, String subtitle, String type, boolean isRead) {
        this.title = title;
        this.subtitle = subtitle;
        this.type = type;
        this.isRead = isRead;
    }

    public String getTitle() { return title; }
    public String getSubtitle() { return subtitle; }
    public String getType() { return type; }
    public boolean isRead() { return isRead; }
}
