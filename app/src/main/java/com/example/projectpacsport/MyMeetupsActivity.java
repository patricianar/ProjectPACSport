package com.example.projectpacsport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class MyMeetupsActivity extends AppCompatActivity {
    DatabaseHelper myDatabaseHelper;
    private ArrayList<Event> myMeetups = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meetups);

        // Initialize BottomNavigationView
        initBottomNavigationView();

        final TextView textViewTitle = findViewById(R.id.textViewTitle);
        textViewTitle.setText("My Meetups");
        final Switch switchMeetup = findViewById(R.id.switchMeetup);
        switchMeetup.setText("Change to Meetups I'm Hosting");

        SharedPreferences pref = getSharedPreferences("SessionUser", MODE_PRIVATE);
        final int currentUser = pref.getInt("UserId", 0);

        myDatabaseHelper = new DatabaseHelper(MyMeetupsActivity.this);
        myMeetups = myDatabaseHelper.getMyMeetups(currentUser, 1);

        final MyListMeetupsAdapter myAdapter = new MyListMeetupsAdapter(myMeetups);
        final RecyclerView recyclerView = findViewById(R.id.recyclerViewMeetups);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);

        switchMeetup.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    textViewTitle.setText("My Hosting Meetups");
                    switchMeetup.setText("Change to Meetups I'm Going");
                    myMeetups.clear();
                    myMeetups = myDatabaseHelper.getMyMeetups(currentUser, 0);
                    MyListMeetupsAdapter mAdapter = new MyListMeetupsAdapter(myMeetups);
                    recyclerView.setAdapter(mAdapter);
                } else {
                    textViewTitle.setText("My Meetups");
                    switchMeetup.setText("Change to Meetups I'm Hosting");
                    myMeetups.clear();
                    myMeetups = myDatabaseHelper.getMyMeetups(currentUser, 1);
                    MyListMeetupsAdapter mAdapter = new MyListMeetupsAdapter(myMeetups);
                    recyclerView.setAdapter(mAdapter);
                }
            }
        });
    }

    /**
     * Init BottomNavigationView with 4 items:
     * results, create event, search event, profile menu
     */
    private void initBottomNavigationView() {
        //Initialize and Assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Result Selector
        bottomNavigationView.setSelectedItemId(R.id.profile_menu);

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
                        startActivity(new Intent(getApplicationContext(), MyMeetupsActivity.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
}