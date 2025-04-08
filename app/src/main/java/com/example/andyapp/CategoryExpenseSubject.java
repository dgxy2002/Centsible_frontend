package com.example.andyapp;

import com.example.andyapp.models.GetCategoryExpenseModels;

import java.util.ArrayList;

public class CategoryExpenseSubject implements DataSubject<GetCategoryExpenseModels>{
    private ArrayList<DataObserver<GetCategoryExpenseModels>> observers;

    public CategoryExpenseSubject(ArrayList<DataObserver<GetCategoryExpenseModels>> observers) {
        this.observers = observers;
    }

    @Override
    public void registerObserver(DataObserver<GetCategoryExpenseModels> observer) {
        observers.add(observer);
    }

    @Override
    public void unregisterObserver(DataObserver<GetCategoryExpenseModels> observer) {
        observers.remove(observer);
    }

    @Override
    public void notifyObservers(GetCategoryExpenseModels data) {
        for (DataObserver<GetCategoryExpenseModels> observer: observers){
            observer.updateData(data);
        }
    }
}
