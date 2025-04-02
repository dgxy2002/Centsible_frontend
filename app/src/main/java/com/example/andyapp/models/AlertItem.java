package com.example.andyapp.models;

public class AlertItem {
    public String title;
    public String subtitle;
    public String type; // "spend_alert", "money_request", "reminder"

    public String getTitle() {
        return title;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getType() {
        return type;
    }

    public AlertItem(String title, String subtitle, String type) {
        this.title = title;
        this.subtitle = subtitle;
        this.type = type;
    }
}
