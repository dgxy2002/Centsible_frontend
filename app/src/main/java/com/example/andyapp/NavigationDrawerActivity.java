package com.example.andyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.andyapp.fragments.DashboardFragment;
import com.example.andyapp.fragments.GroupsFragment;
import com.google.android.material.navigation.NavigationView;

public class NavigationDrawerActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton btnMenu;
    ImageButton btnBarRight;
    NavigationView drawerNavView;
    String userid;
    String viewerid;
    String token;
    Intent intent;
    int fragmentState;
    SharedPreferences mypref;
    ImageButton btnNavStreaks;
    View headerView;

    private static final String TAG = "LOGCAT";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navigation_drawer);
        btnMenu = findViewById(R.id.btnMenu);
        btnBarRight = findViewById(R.id.btnBarRight);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerNavView = findViewById(R.id.drawerNavView);
        headerView = drawerNavView.getHeaderView(0);
        btnNavStreaks = headerView.findViewById(R.id.navStreaks);
        btnNavStreaks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationDrawerActivity.this, StreaksActivity.class);
                startActivity(intent);
            }
        });
        mypref = getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        userid = mypref.getString(LoginActivity.USERKEY, LoginActivity.DEFAULT_USERID);
        viewerid = mypref.getString(LoginActivity.VIEWERKEY, LoginActivity.DEFAULT_USERID);
        token = mypref.getString(LoginActivity.TOKENKEY, "None");
        Log.d(TAG, String.format("USERID IN NAVDRAWER %s", userid));
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });

        btnBarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationDrawerActivity.this, ActivityActivity.class);
                startActivity(intent);
            }
        });

        drawerNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();
                if (itemid == R.id.navDashboard) {
                    changeFragment(new DashboardFragment());
                } else if (itemid == R.id.navLB) {
                    Intent intent = new Intent(NavigationDrawerActivity.this, MainActivity.class);
                    startActivity(intent);
                } else if (itemid == R.id.navGroups) {
                    changeFragment(new GroupsFragment());
                } else if (itemid == R.id.navSettings) {
                    Toast.makeText(NavigationDrawerActivity.this, "settings", Toast.LENGTH_SHORT).show();
                } else if (itemid == R.id.navLogout) {
                    Intent intent = new Intent(NavigationDrawerActivity.this, LoginActivity.class);
                    startActivity(intent);
                }
                drawerLayout.close();
                return false;
            }
        });
    }


    public void changeFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        Bundle arguments = new Bundle();
        arguments.putString(LoginActivity.USERKEY, userid);
        arguments.putString(LoginActivity.VIEWERKEY, viewerid);
        arguments.putString(LoginActivity.TOKENKEY, token);
        fragment.setArguments(arguments);
        transaction.replace(R.id.dashboardFragmentContainer, fragment);
        transaction.commit();
    }
}