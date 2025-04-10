package com.example.andyapp;

import com.example.andyapp.models.GetCategoryExpenseModels;

import java.util.ArrayList;

public class DataSubject<T> {
    private ArrayList<DataObserver<T>> observers;
    public DataSubject() {
        this.observers = new ArrayList<>();
    }
    public void registerObserver(DataObserver<T> observer) {
        observers.add(observer);
    }
    public void unregisterObserver(DataObserver<T> observer) {
        observers.remove(observer);
    }
    public void notifyObservers(T data) {
        for (DataObserver<T> observer: observers){
            observer.updateData(data);
        }
    }
}
