package com.example.andyapp.models;

public class InvitationModel {
    private int image;
    private String inviterName;
    private String inviterId;

    public InvitationModel(int image, String inviterName, String inviterId) {
        this.image = image;
        this.inviterName = inviterName;
        this.inviterId = inviterId;
    }

    public int getImage() {
        return image;
    }

    public String getInviterName() {
        return inviterName;
    }

    public String getInviterId() {
        return inviterId;
    }
}
