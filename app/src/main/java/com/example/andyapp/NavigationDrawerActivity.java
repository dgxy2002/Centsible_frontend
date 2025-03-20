package com.example.andyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.andyapp.fragments.IncomeFragment;

public class NavigationDrawerActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton btnMenu;
    ImageButton btnBarRight;
    int fragmentState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        fragmentState = 0;
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navigation_drawer);
        btnMenu = findViewById(R.id.btnMenu);
        btnBarRight = findViewById(R.id.btnBarRight);
        drawerLayout = findViewById(R.id.drawerLayout);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

       btnBarRight.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               FragmentManager fragmentManager = getSupportFragmentManager();
               FragmentTransaction transaction = fragmentManager.beginTransaction();
               transaction.replace(R.id.dashboardFragmentContainer, new IncomeFragment());
               transaction.commit();
           }
       }


       );

    }
}