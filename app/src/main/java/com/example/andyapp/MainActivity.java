package com.example.andyapp;

import android.os.Bundle;

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

    int[] profilePictures = {R.drawable.person_icon, R.drawable.person_icon, R.drawable.person_icon,R.drawable.person_icon,
            R.drawable.person_icon,R.drawable.person_icon,R.drawable.person_icon,R.drawable.person_icon,
            R.drawable.person_icon,R.drawable.person_icon,};
    int[] arrowUpDown = {R.drawable.arrow_up, R.drawable.arrow_up, R.drawable.arrow_up, R.drawable.arrow_up,
            R.drawable.arrow_up, R.drawable.arrow_up, R.drawable.arrow_up, R.drawable.arrow_up,
            R.drawable.arrow_up, R.drawable.arrow_up,};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
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
    }
    private void setUpUsername(){
        String[] usernameNames = getResources().getStringArray(R.array.name_list);
        String[] pointsList = getResources().getStringArray(R.array.points_list);
        String[] numberUpDown = getResources().getStringArray(R.array.limited_numbers);

        for(int i = 0; i< usernameNames.length; i++){
            username.add(new UsernameModel(usernameNames[i],
                    pointsList[i], numberUpDown[i], profilePictures[i], arrowUpDown[i]));


        }


    }
}