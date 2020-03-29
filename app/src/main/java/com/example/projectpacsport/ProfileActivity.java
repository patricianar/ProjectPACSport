package com.example.projectpacsport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class ProfileActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private static final String TAG = "ProfileActivity";
    Context mContext = ProfileActivity.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // Initialize BottomNavigationView
        initBottomNavigationView();

        // Initialize the ListView
        initTaskListView();
    }

    /**
     * Init TaskListView with 3 items:
     * calendar, history, favorite team item.
     * Can add more when needed
     */
    private void initTaskListView() {
        ListView mListView = findViewById(R.id.listView_profile);

        // Create ProfileTaskItem objects and add to mListView
        ProfileTaskItem calendarItem = new ProfileTaskItem(R.drawable.ic_calendar, "Event calendar");
        ProfileTaskItem historyItem = new ProfileTaskItem(R.drawable.ic_history, "History events");
        ProfileTaskItem favTeamsItem = new ProfileTaskItem(R.drawable.ic_favorite_grey, "Favorite teams");

        List<ProfileTaskItem> itemList = new ArrayList<>();
        itemList.add(calendarItem);
        itemList.add(historyItem);
        itemList.add(favTeamsItem);

        ProfileListAdapter adapter = new ProfileListAdapter(this, R.layout.layout_profile_adapter_view, itemList);
        mListView.setAdapter(adapter);

        mListView.setOnItemClickListener(this);
    }


    /**
     * Switch to a new activity base on the position item in the list view
     * @param parent AdapterView
     * @param view current view of an item
     * @param position position of an item in the list
     * @param id is the id of an item
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        switch (position) {
            case 0:
                startActivity(new Intent(mContext, CalendarActivity.class));
                break;
            case 1:
                startActivity(new Intent(mContext, HistoryEventsActivity.class));
                break;
            case 2:
                startActivity(new Intent(mContext, FavoriteTeamActivity.class));
                break;
        }
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
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
}
