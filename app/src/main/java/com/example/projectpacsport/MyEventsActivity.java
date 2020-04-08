package com.example.projectpacsport;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class MyEventsActivity extends AppCompatActivity implements MyEventsFragment.OnFragInteractionListener {
    FragmentManager manager;
    MyEventsFragment myEventsFragment, myHostingEventsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meetups);

        // Initialize BottomNavigationView
        initBottomNavigationView();

        manager = getSupportFragmentManager();
        myEventsFragment = MyEventsFragment.newInstance(1);
        myHostingEventsFragment = MyEventsFragment.newInstance(0);
        manager.beginTransaction().add(R.id.frameMyEvents, myEventsFragment).addToBackStack(null).commit();

        manager.beginTransaction().add(R.id.frameMyEvents, myHostingEventsFragment).addToBackStack(null).commit();
        showHideFragment(myHostingEventsFragment);
    }

    public void showHideFragment(final Fragment fragment) {
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);

        if (fragment.isHidden()) {
            fragTrans.show(fragment);
        } else {
            fragTrans.hide(fragment);
        }
        fragTrans.commit();
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
                        startActivity(new Intent(getApplicationContext(), MyEventsActivity.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }

    @Override
    public void OnSwitchListener(int choiceSwitch) {
        FragmentTransaction fragTrans = getSupportFragmentManager().beginTransaction();
        fragTrans.setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out);
        if (choiceSwitch == 0) {
            fragTrans.show(myHostingEventsFragment);
            fragTrans.hide(myEventsFragment);
        } else {
            fragTrans.show(myEventsFragment);
            fragTrans.hide(myHostingEventsFragment);
        }
        myEventsFragment.SwitchValue(false);
        myHostingEventsFragment.SwitchValue(true);
        fragTrans.commit();
    }
}