package com.example.andyapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.andyapp.fragments.DashboardFragment;
import com.example.andyapp.fragments.ExpenseFragment;
import com.example.andyapp.fragments.GroupsFragment;
import com.example.andyapp.fragments.InvitationsFragment;
import com.example.andyapp.fragments.LogExpenseFragment;
import com.example.andyapp.queries.NotificationService;
import com.google.android.material.navigation.NavigationView;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class NavigationDrawerActivity extends AppCompatActivity {
    DrawerLayout drawerLayout;
    ImageButton btnMenu;
    ImageButton btnBarRight;
    NavigationView drawerNavView;
    String userId;
    String username;
    String viewerId;
    String token;
    Intent intent;
    int fragmentState;
    SharedPreferences mypref;
    ImageButton btnNavStreaks;
    View headerView;
    TextView toolbarTitle;
    TextView drawerHeaderTextView;
    public static String FRAGMENT_TAG = "FRAGMENT_TAG";
    public static String CONNECTION_NAME_TAG = "CONNECTION_NAME_TAG";
    String targetFragmentName;
    NotificationService notificationService;
    BtnBarRightObserver btnBarRightObserver;

    private static final String TAG = "LOGCAT";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_navigation_drawer);
        targetFragmentName = getIntent().getStringExtra("FRAGMENT_TAG");
        btnMenu = findViewById(R.id.btnMenu);
        btnBarRight = findViewById(R.id.btnBarRight);
        toolbarTitle = findViewById(R.id.toolbarTitle);
        drawerLayout = findViewById(R.id.drawerLayout);
        drawerNavView = findViewById(R.id.drawerNavView);
        headerView = drawerNavView.getHeaderView(0);
        resetToolBar();
        if (targetFragmentName != null && targetFragmentName.equals("LogExpense")) {
            toolbarTitle.setText("Log Expense");
            btnMenu.setImageResource(R.drawable.arrow_back);
            btnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    resetToolBar();
                    changeFragment(new DashboardFragment());
                }
            });
            changeFragment(new LogExpenseFragment());
        }

        //If I navigated here via the groups page
        String connectionName = getIntent().getStringExtra(CONNECTION_NAME_TAG);
        if (connectionName != null) {
            toolbarTitle.setText(String.format("%s's Dashboard", connectionName));
        }

        //Permissions from Shared Preferences
        mypref = getSharedPreferences(LoginActivity.PREFTAG, Context.MODE_PRIVATE);
        userId = mypref.getString(LoginActivity.USERKEY, LoginActivity.DEFAULT_USERID);
        viewerId = mypref.getString(LoginActivity.VIEWERKEY, LoginActivity.DEFAULT_USERID);
        username = mypref.getString(LoginActivity.USERNAMEKEY, LoginActivity.DEFAULT_USERNAME);
        token = mypref.getString(LoginActivity.TOKENKEY, "None");

        drawerHeaderTextView = headerView.findViewById(R.id.headerTextView);
        drawerHeaderTextView.setText(String.format("%s's Menu", username));


        btnNavStreaks = headerView.findViewById(R.id.navStreaks);
        btnNavStreaks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationDrawerActivity.this, StreaksActivity.class);
                startActivity(intent);
            }
        });


        //If there are unread notifications, change BtnBarRight ImageResource if current fragment is dashboard.
        notificationService = new NotificationService(NavigationDrawerActivity.this);
        btnBarRightObserver = new BtnBarRightObserver();
        fetchUnreadNotifications(); //Make sure this comes after resetToolBar or else itll get reset

        //Set up Navigation Drawer
        drawerNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int itemid = item.getItemId();
                changeToolBar(itemid);
                if (itemid == R.id.navDashboard) {
                    SharedPreferences.Editor editor = mypref.edit();
                    editor.putString(LoginActivity.VIEWERKEY, userId);
                    editor.apply();
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


    public void changeFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.dashboardFragmentContainer, fragment);
        transaction.commit();
    }
    //Logic to dynamically change the toolbar
    public void changeToolBar(int itemId) {
        if (itemId == R.id.navGroups) {
            toolbarTitle.setText("Groups");
            btnMenu.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    drawerLayout.open();
                }
            });
            btnBarRight.setImageResource(R.drawable.message_icon);
            btnBarRight.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(NavigationDrawerActivity.this, "Bringing you to invitations", Toast.LENGTH_SHORT).show();
                    btnMenu.setImageResource(R.drawable.arrow_back);
                    btnMenu.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            resetToolBar();
                            changeFragment(new GroupsFragment());
                        }
                    });
                    btnBarRight.setEnabled(false);
                    btnBarRight.setVisibility(View.INVISIBLE);
                    changeFragment(new InvitationsFragment());
                    toolbarTitle.setText("Invitations");
                }
            });
        } else {
            resetToolBar();
        }
    }

    public void resetToolBar() {
        btnBarRight.setImageResource(R.drawable.bell);
        btnBarRight.setEnabled(true);
        btnBarRight.setVisibility(View.VISIBLE);
        toolbarTitle.setText("Insights");
        btnBarRight.setScaleType(ImageView.ScaleType.FIT_CENTER);
        btnBarRight.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(NavigationDrawerActivity.this, ActivityActivity.class);
                startActivity(intent);
            }
        });
        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.open();
            }
        });
        btnMenu.setImageResource(R.drawable.hamburgermenu);
    }

    public void fetchUnreadNotifications() {
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Looper looper = Looper.getMainLooper();
        Handler handler = new Handler(looper);
        Log.d(TAG, "UserID FOR NOTIFICATIONS" + userId);
        executor.execute(new Runnable() {
            @Override
            public void run() {
                notificationService.fetchUnreadNotificationCount(token, userId, handler, btnBarRightObserver);
            }
        });
    }

    class BtnBarRightObserver implements DataObserver<Integer> {
        @Override
        public void updateData(Integer data) {
            Log.d(TAG, "Notifications working" + data);
            if (getSupportFragmentManager().findFragmentById(R.id.dashboardFragmentContainer) instanceof DashboardFragment) {
                if (data > 0) {
                    btnBarRight.setImageResource(R.drawable.unreadnotifications);
                    btnBarRight.setScaleType(ImageView.ScaleType.CENTER_CROP);
                }
            }
        }
    }
}