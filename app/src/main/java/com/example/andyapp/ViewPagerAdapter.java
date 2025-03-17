package com.example.andyapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.example.andyapp.fragments.BudgetFragment;
import com.example.andyapp.fragments.ExpenseFragment;
import com.example.andyapp.fragments.IncomeFragment;

public class ViewPagerAdapter extends FragmentStateAdapter {
    public ViewPagerAdapter(@NonNull Fragment fragment){
        super(fragment);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position){
            case 0:
                return new ExpenseFragment();
            case 1:
                return new IncomeFragment();
            case 2:
                return new BudgetFragment();
            default:
                return new ExpenseFragment();
        }
    }
    @Override
    public int getItemCount() {
        return 3;
    }
}
