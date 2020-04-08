package com.example.projectpacsport;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;


/**
 * A simple {@link Fragment} subclass.
 */
public class MyEventsFragment extends Fragment {
    DatabaseHelper myDatabaseHelper;
    private ArrayList<Event> myEvents= new ArrayList<>();
    ConstraintLayout constraintLayout;
    RecyclerView recyclerView;
    int currentUser;
    Switch switchEvent;

     OnFragInteractionListener mListener;

    interface OnFragInteractionListener{
        void OnSwitchListener(int choiceSwitch);
    }
    private static final String CHOICE = "choice";

    public MyEventsFragment() {
        // Required empty public constructor
    }

    public static MyEventsFragment newInstance(int choice){
        MyEventsFragment fragment = new MyEventsFragment();
        Bundle arguments = new Bundle();
        arguments.putInt(CHOICE, choice);
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_my_events, container, false);
        SharedPreferences pref = getActivity().getSharedPreferences("SessionUser", MODE_PRIVATE);
        currentUser = pref.getInt("UserId", 0);
        constraintLayout = view.findViewById(R.id.layoutMyEvents);
        myDatabaseHelper = new DatabaseHelper(getContext());
        TextView textViewTitle = view.findViewById(R.id.textViewTitle);
        switchEvent = view.findViewById(R.id.switchMeetup);

        if(getArguments().containsKey(CHOICE)){
            int mChoice = getArguments().getInt(CHOICE);
            if(mChoice == 0){
                textViewTitle.setText("My Hosting Events");
                switchEvent.setText("Change to Events I'm Going");
                switchEvent.setChecked(true);
                myEvents = myDatabaseHelper.getMyMeetups(currentUser, 0);
                MyListMeetupsAdapter myAdapter = new MyListMeetupsAdapter(myEvents);
                recyclerView = view.findViewById(R.id.recyclerViewMyEvents);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(myAdapter);
                switchEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mListener.OnSwitchListener(1);
                    }
                });
            }
            else{
                textViewTitle.setText("My Events");
                switchEvent.setText("Change to Events I'm Hosting");
                switchEvent.setChecked(false);
                myEvents = myDatabaseHelper.getMyMeetups(currentUser, 1);
                MyListMeetupsAdapter myAdapter = new MyListMeetupsAdapter(myEvents);
                recyclerView = view.findViewById(R.id.recyclerViewMyEvents);
                recyclerView.setHasFixedSize(true);
                recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
                recyclerView.setAdapter(myAdapter);
                enableSwipeToDeleteAndUndo(myAdapter);
                switchEvent.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        mListener.OnSwitchListener(0);
                    }
                });
            }

        }
        return view;
    }

    public void SwitchValue(boolean switchStatus){
        switchEvent.setChecked(switchStatus);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof OnFragInteractionListener){
            mListener = (OnFragInteractionListener)context;
        }else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragInteractionListener");
        }
    }

    private void enableSwipeToDeleteAndUndo(final MyListMeetupsAdapter myAdapter) {
        SwipeToDeleteCallback swipeToDeleteCallback = new SwipeToDeleteCallback(getContext()) {
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {

                final int position = viewHolder.getAdapterPosition();
                final Event event = myAdapter.getData().get(position);

                myAdapter.removeItem(position);
                myDatabaseHelper.removeEventAttendance(currentUser, event.getId());

                Snackbar snackbar = Snackbar.make(constraintLayout, "Event was removed from My Events", Snackbar.LENGTH_LONG);
                snackbar.setAction("UNDO", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        myAdapter.restoreItem(event, position);
                        recyclerView.scrollToPosition(position);
                        myDatabaseHelper.addEventAttendance(currentUser, event.getId());
                    }
                });

                snackbar.setActionTextColor(Color.WHITE);
                snackbar.show();
            }
        };

        ItemTouchHelper itemTouchhelper = new ItemTouchHelper(swipeToDeleteCallback);
        itemTouchhelper.attachToRecyclerView(recyclerView);
    }
}
