package com.example.andyapp.managers;

import android.content.Context;
import android.graphics.LinearGradient;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class ScaleCenterItemLayoutManager extends LinearLayoutManager {
    Context context;
    public ScaleCenterItemLayoutManager(Context context) {
        super(context);
    }

    public ScaleCenterItemLayoutManager(Context context, int orientation, boolean ReverseLayout){
        super(context, orientation, ReverseLayout);
    }

    @Override
    public boolean checkLayoutParams(RecyclerView.LayoutParams lp) {
        lp.width = getWidth()/3; // Each item will have 1/3 Recycler width
        return super.checkLayoutParams(lp);
    }

    @Override
    public void onLayoutCompleted(RecyclerView.State state) {
        super.onLayoutCompleted(state);
        scaleMiddleItem();
    }

    @Override
    public int scrollHorizontallyBy(int dx, RecyclerView.Recycler recycler, RecyclerView.State state) {
        int scrolled =  super.scrollHorizontallyBy(dx, recycler, state);
        if (getOrientation() == HORIZONTAL){
            scaleMiddleItem();
            return scrolled;
        }else{
            return 0;
        }
    }

    private void scaleMiddleItem(){
        float mid = getWidth()/2.0f;
        float d1 = 0.9f * mid;
        for(int i = 0; i < getChildCount(); i++){
            View child = getChildAt(i);
            float childMid = (getDecoratedLeft(child) + getDecoratedRight(child))/ 2.0f;
            float d = Math.min(d1,Math.abs(mid - childMid));
            float scale = 1f - 0.15f * d/d1;
            child.setScaleX(scale);
            child.setScaleY(scale);
        }
    }
}
