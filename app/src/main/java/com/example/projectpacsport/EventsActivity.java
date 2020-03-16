package com.example.projectpacsport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class EventsActivity extends AppCompatActivity {
    private ArrayList<Event> events = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        Event a = new Event();
        a.setCapacity(5);
        a.setLocation("hoise");
        a.setTeam1Id(1);
        a.setTeam2Id(2);
        events.add(a);
        events.add(a);
        events.add(a);
        events.add(a);

        MyListEventsAdapter myAdapter = new MyListEventsAdapter(events);
        RecyclerView recyclerView = findViewById(R.id.recyclerViewEvents);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(myAdapter);
    }
}
