package com.example.andyapp.models;

public class NotificationResponse {
    private String id;
    private String userId;
    private String message;
    private String senderUsername;
    private String createdAt;
    private boolean read;

    public String getId() { return id; }
    public String getUserId() { return userId; }
    public String getMessage() { return message; }
    public String getSenderUsername() { return senderUsername; }
    public String getCreatedAt() { return createdAt; }
    public boolean isRead() { return read; }
}
