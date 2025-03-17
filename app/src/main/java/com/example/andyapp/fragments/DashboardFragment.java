package com.example.andyapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.andyapp.R;
import com.example.andyapp.ViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;
import android.util.Log;
/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardFragment extends Fragment {
    TabLayout tabLayout;
    ViewPager2 viewPagerDashboard ;
    ViewPagerAdapter viewPagerAdapter;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_dashboard, container, false);
        tabLayout = view.findViewById(R.id.transTab);
        viewPagerDashboard = view.findViewById(R.id.viewPagerDashboard);
        viewPagerAdapter = new ViewPagerAdapter(this);
        viewPagerDashboard.setAdapter(viewPagerAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Toast.makeText(getContext(), "Displaying fragment", Toast.LENGTH_SHORT).show();
                Log.w("debugging", "Displaying fragment");
                viewPagerDashboard.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        // Inflate the layout for this fragment
        return view;
    }
}