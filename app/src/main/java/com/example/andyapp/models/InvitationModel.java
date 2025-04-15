package com.example.andyapp.models;

public class InvitationModel {
    private String imageUrl;
    private String inviterName;
    private String inviterId;

    public InvitationModel(String imageUrl, String inviterName, String inviterId) {
        this.imageUrl = imageUrl;
        this.inviterName = inviterName;
        this.inviterId = inviterId;
    }

    public String getImage() {
        return imageUrl;
    }

    public String getInviterName() {
        return inviterName;
    }

    public String getInviterId() {
        return inviterId;
    }
}
