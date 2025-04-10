package com.example.andyapp.utils;

import com.example.andyapp.models.GetCategoryExpenseModel;

import java.util.Comparator;

public class SortExpenseByName implements Comparator<GetCategoryExpenseModel> {
    @Override
    public int compare(GetCategoryExpenseModel model1, GetCategoryExpenseModel model2) {
        return model1.getCategory().compareToIgnoreCase(model2.getCategory());
    }

    @Override
    public Comparator<GetCategoryExpenseModel> reversed() {
        return Comparator.super.reversed();
    }
}
