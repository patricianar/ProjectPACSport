package com.example.projectpacsport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class EventsActivity extends AppCompatActivity {
    private ArrayList<Event> events = new ArrayList<>();
    private ArrayList<Integer> myEvents = new ArrayList<>();
    DatabaseHelper myDatabaseHelper;
    int currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        // Initialize BottomNavigationView
        initBottomNavigationView();

        SharedPreferences pref = getSharedPreferences("SessionUser", MODE_PRIVATE);
        currentUser = pref.getInt("UserId", 0);

        myDatabaseHelper = new DatabaseHelper(EventsActivity.this);
        myEvents = myDatabaseHelper.getMyEventsIds(currentUser);
        events = myDatabaseHelper.getEventRecs(myEvents);
//        events = mService.getEventRecs(myEvents);

//        for(int eventId: myEvents){
//            events.get(eventId).setSelected(true);
//        }
//
//        Collection<Event> values = events.values();
//        ArrayList<Event> eventsList = new ArrayList<>(values);

        MyListEventsAdapter myAdapter = new MyListEventsAdapter(events);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewEvents);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }



    /**
     * Init BottomNavigationView with 4 items:
     * results, create event, search event, profile menu
     */
    private void initBottomNavigationView() {
        //Initialize and Assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Result Selector
        bottomNavigationView.setSelectedItemId(R.id.search_event);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.results:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish(); // avoid going back to the same selected tab many times - save memory
                        return true;
                    case R.id.create_event:
                        startActivity(new Intent(getApplicationContext(), RegisterEvents.class));
                        finish();
                        return true;
                    case R.id.search_event:
                        startActivity(new Intent(getApplicationContext(), EventsActivity.class));
                        finish();
                        return true;
                    case R.id.profile_menu:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
}
