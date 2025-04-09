package com.example.andyapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<UsernameModel> username = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_leaderboard);
        RecyclerView recyclerView = findViewById(R.id.leaderboard_recycler_view);
        setUpUsername();
        UsernameModelRCV adapter = new UsernameModelRCV(this, username);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main_layout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ImageButton btn_back = findViewById(R.id.btn_back);

        btn_back.setOnClickListener(view -> {
            Intent subActivityIntent = new Intent(view.getContext(), NavigationDrawerActivity.class);
            startActivity(subActivityIntent);
        });
    }
    private void setUpUsername(){
        String[] usernameNames = getResources().getStringArray(R.array.name_list);
        String[] pointsList = getResources().getStringArray(R.array.points_list);
        String[] numberUpDown = getResources().getStringArray(R.array.limited_numbers);

        for(int i = 0; i< usernameNames.length; i++){
            username.add(new UsernameModel(usernameNames[i],
                    pointsList[i], numberUpDown[i], R.drawable.avatar, R.drawable.arrow_up));

        }

    }
}