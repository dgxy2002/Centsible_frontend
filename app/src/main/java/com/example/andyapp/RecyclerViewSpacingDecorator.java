package com.example.andyapp;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewSpacingDecorator extends RecyclerView.ItemDecoration {
    private final int verticalSpacing;

    public RecyclerViewSpacingDecorator(int verticalSpacing) {
        this.verticalSpacing = verticalSpacing;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.bottom = verticalSpacing;

    }
}
