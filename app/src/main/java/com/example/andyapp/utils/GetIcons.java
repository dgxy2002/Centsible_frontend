package com.example.andyapp.utils;

import android.content.Context;

import com.example.andyapp.R;

import java.util.HashMap;

public class GetIcons implements GetIconInterface{
    public int getIcon(String category){
        switch (category){
            case "Education":
                return R.drawable.educationicon;
            case "Entertainment":
                return R.drawable.video;
            case "Gifts":
                return R.drawable.gifticon;
            case "Transport":
                return R.drawable.transporticon;
            case "Others":
                return R.drawable.othersicon;
            case "Subscription":
                return R.drawable.subscriptionicon;
            case "Shopping":
                return R.drawable.shoppingicon;
            case "Food":
                return R.drawable.foodicon;
            default:
                return R.drawable.othersicon;
        }
    }
}
