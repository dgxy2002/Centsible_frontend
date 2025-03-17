package com.example.andyapp;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        Button btnLogExp = findViewById(R.id.btnLogExp);
        btnLogExp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myInt = new Intent(getApplicationContext(), LogExpense.class);
                startActivity(myInt);
            }
        });
        Button btnDashboard = findViewById(R.id.btnDashboard);
        btnDashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myInt = new Intent(getApplicationContext(), Dashboard.class);
                startActivity(myInt);
            }
        });
        Button btnNavMenu = findViewById(R.id.btnNavMenu);
        btnNavMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myInt = new Intent(getApplicationContext(), NavigationDrawerActivity.class);
                startActivity(myInt);
            }
        });
    }
}

//TODO Build dropdown menu
//TODO Change font of dropdown menu
//TODO Add icons for dropdown menu
//TODO Add functionality for buttons (states to key in amount of money, date and to route to server)