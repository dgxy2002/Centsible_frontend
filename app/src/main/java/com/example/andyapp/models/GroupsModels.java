package com.example.andyapp.models;

import java.util.ArrayList;

public class GroupsModels {
    private ArrayList<GroupsModel> groupsModels;

    public GroupsModels(ArrayList<GroupsModel> groupsModels) {
        this.groupsModels = groupsModels;
    }

    public ArrayList<GroupsModel> getGroupsModels() {
        return groupsModels;
    }

    public void setGroupsModels(ArrayList<GroupsModel> groupsModels) {
        this.groupsModels = groupsModels;
    }

    public void addGroupModel(GroupsModel model){
        groupsModels.add(model);
    }

    public void deleteGroupModel(GroupsModel model){
        groupsModels.remove(model);
    }
}
