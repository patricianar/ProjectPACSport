package com.example.projectpacsport;
// This page for Introduction add by Sheradil Aibekov

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TableLayout;

import com.example.projectpacsport.login.LoginActivity;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity1 extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter;
    TabLayout tabIndicator;
    Button btnNext;
    int position=0;
    Button btnGetStarted;
    Animation btnAnim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //make the activity on full screen

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_intro1);

        //when this activity is about to be launch, check if its opened before or not
        if (restorePrefData()){
            Intent loginIntent= new Intent(getApplicationContext(), LoginActivity.class);
            startActivity(loginIntent);
            finish();
        }

        //tab indicator view

        tabIndicator=findViewById(R.id.tab_indicator);
        btnGetStarted=findViewById(R.id.btn_getStarted);
        btnNext=findViewById(R.id.btn_intro_next);
        btnAnim= AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_animation);

        //fill the list Screen
        final List<ScreenItem> mList=new ArrayList<>();
        mList.add(new ScreenItem("Game Results", "Here you will find the latest game result of NBA, NHL, MLB", R.drawable.game_results));
        mList.add(new ScreenItem("Create Event", "Do you want to create an event? In this app, you can create an event in few clicks!", R.drawable.create_event));
        mList.add(new ScreenItem("Search for an Event", "Find an opportunity to demonstrate you skills!", R.drawable.search_event));



        //setup viewpager
        screenPager=findViewById(R.id.screen_viewpager);
        introViewPagerAdapter=new IntroViewPagerAdapter(this, mList);
        screenPager.setAdapter(introViewPagerAdapter);

        //setup tablayout with viewpager

        tabIndicator.setupWithViewPager(screenPager);

        //next button click Listener

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                position=screenPager.getCurrentItem();
                if (position<mList.size()){

                    position++;
                    screenPager.setCurrentItem(position);
                }

                if (position==mList.size()-1){// when we will reach last screen

                    loadLastScreen();

                }
            }
        });

        //tabLayout and change listener
        tabIndicator.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {

                if (tab.getPosition()==mList.size()-1){
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //Get started Button click listener

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //open main activity
                Intent loginIntent=new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(loginIntent);

                //save boolean value to storage so next time when user run the app
                //we could know that he is already checked the intro screen activity
                //Sheradil used sharedPref to that process

                savePrefsDate();
                finish();
            }
        });

    }

    private boolean restorePrefData() {
        SharedPreferences pref=getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        Boolean isIntroActivityOpenedBefore=pref.getBoolean("isIntroOpened",false);
        return isIntroActivityOpenedBefore;
    }

    private void savePrefsDate(){
        SharedPreferences pref=getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("isIntroOpened", true);
        editor.commit();
    }
//show the GETSTARTED Button and hide the indicator and next button
    private void loadLastScreen(){

        btnNext.setVisibility(View.INVISIBLE);
        btnGetStarted.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.INVISIBLE);


        //animation setup

        btnGetStarted.setAnimation(btnAnim);
    }
}
