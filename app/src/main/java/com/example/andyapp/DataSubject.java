package com.example.andyapp;

public interface DataSubject<T> {
    void registerObserver(DataObserver<T> observer);
    void unregisterObserver(DataObserver<T> observer);
    void notifyObservers(T data);
}
