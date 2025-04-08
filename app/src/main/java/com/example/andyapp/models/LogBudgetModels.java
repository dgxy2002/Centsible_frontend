package com.example.andyapp.models;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class LogBudgetModels {
    private ArrayList<LogBudgetModel> logBudgetModels;
    public LogBudgetModels() {
        this.logBudgetModels = new ArrayList<>();
    }
    public ArrayList<LogBudgetModel> getLogBudgetModels() {
        return logBudgetModels;
    }

    public void setLogBudgetModels(ArrayList<LogBudgetModel> logBudgetModels) {
        this.logBudgetModels = logBudgetModels;
    }

    public void addLogBudgetModel(LogBudgetModel model){
        this.logBudgetModels.add(model);
    }

    public void updateBudgetModel(int id, String budget){
        logBudgetModels.get(id).setBudget(budget);
    }
}
