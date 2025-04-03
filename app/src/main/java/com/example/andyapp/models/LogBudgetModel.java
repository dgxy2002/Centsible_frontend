package com.example.andyapp.models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

public class LogBudgetModel implements Parcelable {
    String category;
    int icon;
    String budget;
    int id;

    public LogBudgetModel(int id, String category, int icon, String budget) {
        this.id = id;
        this.category = category;
        this.icon = icon;
        this.budget = budget;
    }

    protected LogBudgetModel(Parcel in) {
        category = in.readString();
        icon = in.readInt();
        budget = in.readString();
        id = in.readInt();
    }

    public static final Creator<LogBudgetModel> CREATOR = new Creator<LogBudgetModel>() {
        @Override
        public LogBudgetModel createFromParcel(Parcel in) {
            return new LogBudgetModel(in);
        }

        @Override
        public LogBudgetModel[] newArray(int size) {
            return new LogBudgetModel[size];
        }
    };

    public int getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public int getIcon() {
        return icon;
    }

    public String getBudget() {
        return budget;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public void setBudget(String budget) {
        this.budget = budget;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel parcel, int i) {
        parcel.writeString(category);
        parcel.writeInt(icon);
        parcel.writeString(budget);
        parcel.writeInt(id);
    }
}
