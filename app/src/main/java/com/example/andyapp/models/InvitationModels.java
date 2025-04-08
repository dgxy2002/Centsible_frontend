package com.example.andyapp.models;

import com.example.andyapp.DataObserver;

import java.util.ArrayList;

public class InvitationModels {
    private ArrayList<InvitationModel> invitationModels;

    public InvitationModels() {
        this.invitationModels = new ArrayList<>();
    }

    public ArrayList<InvitationModel> getInvitationModels() {
        return invitationModels;
    }

    public void setInvitationModels(ArrayList<InvitationModel> invitationModels) {
        this.invitationModels = invitationModels;
    }

    public void updateInvitation(int i){
        invitationModels.remove(i);
    }
}
