package com.example.andyapp.adapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.andyapp.fragments.AlertsFragment;
import com.example.andyapp.fragments.TransactionsFragment;

public class ActivityViewPagerAdapter extends FragmentStateAdapter {

    public ActivityViewPagerAdapter(@NonNull FragmentActivity activity) {
        super(activity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        if (position == 0) return new TransactionsFragment();
        return new AlertsFragment();
    }

    @Override
    public int getItemCount() {
        return 2;
    }
}
