package com.example.andyapp.utils;

import com.example.andyapp.models.GetCategoryExpenseModel;
import com.example.andyapp.models.GetCategoryExpenseModels;

import java.util.Comparator;

public class SortExpenseByAmount implements Comparator<GetCategoryExpenseModel> {
    @Override
    public int compare(GetCategoryExpenseModel model1, GetCategoryExpenseModel model2) {
        return Double.compare(model1.getAmount(), model2.getAmount());
    }

    @Override
    public Comparator<GetCategoryExpenseModel> reversed() {
        return Comparator.super.reversed();
    }
}
