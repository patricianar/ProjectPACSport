package com.example.projectpacsport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.projectpacsport.login.LoginActivity;

public class MainMenu extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        Button btnScore = findViewById(R.id.btnGameResult);
        Button btnEvent = findViewById(R.id.btnEvents);

       btnScore.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(MainMenu.this, MainActivity.class);
               startActivity(intent);
           }
       });

        btnEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainMenu.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
