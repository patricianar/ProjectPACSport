package com.example.projectpacsport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class LandingActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_landing);
        Button btnGames = findViewById(R.id.btnGames);
        Button btnEvents = findViewById(R.id.btnEvents);

        //Initialize and Assign variable
        BottomNavigationView bottomNavigationView=findViewById(R.id.bottom_navigation);

        //Set Result Selector
        bottomNavigationView.setSelectedItemId(R.id.results);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()){
                    case R.id.results:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.create_event:
                        startActivity(new Intent(getApplicationContext(), RegisterEvents.class));
                        overridePendingTransition(0,0);
                        return true;


                }
                return false;
            }
        });

        btnGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });

        btnEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LandingActivity.this, RegisterEvents.class);
                startActivity(intent);
            }
        });
    }
}
