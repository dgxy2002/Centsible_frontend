package com.example.andyapp;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;
import android.content.Intent;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.andyapp.fragments.DashboardFragment;
import com.example.andyapp.fragments.GroupsFragment;
import com.example.andyapp.fragments.IncomeFragment;
import com.google.android.material.navigation.NavigationView;

public class NavigationDrawerActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton btnMenu;
    ImageButton btnBarRight;
    NavigationView drawerNavView;
    int fragmentState;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navigation_drawer);
        btnMenu = findViewById(R.id.btnMenu);
        btnBarRight = findViewById(R.id.btnBarRight);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerNavView = findViewById(R.id.drawerNavView);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

       btnBarRight.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View view) {
               changeFragment(new IncomeFragment());
           }
       });
       drawerNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
           @Override
           public boolean onNavigationItemSelected(@NonNull MenuItem item) {
               int itemid = item.getItemId();
               if (itemid == R.id.navAvatar){
                   changeFragment(new DashboardFragment());
               }else if (itemid == R.id.navLB){
                   Intent intent = new Intent(NavigationDrawerActivity.this, MainActivity.class);
                   startActivity(intent);
               }else if (itemid == R.id.navGroups){
                   changeFragment(new GroupsFragment());
               }else if (itemid == R.id.navSettings){
                   Toast.makeText(NavigationDrawerActivity.this, "settings", Toast.LENGTH_SHORT).show();
               }else if (itemid == R.id.navLogout){
                   Toast.makeText(NavigationDrawerActivity.this, "logout", Toast.LENGTH_SHORT).show();
               }
               drawerLayout.close();
               return false;
           }
       });

    }
    public void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.dashboardFragmentContainer, fragment);
        transaction.commit();
    }
}