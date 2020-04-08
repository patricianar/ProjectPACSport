package com.example.projectpacsport;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class DetailsEventFragment extends Fragment {
    private DatabaseHelper myDatabaseHelper;
    private static final String ARG_PARAM1 = "eventId";

    private String mTitle;
    private int mMeetupId;

    public DetailsEventFragment() {
    }

    public static DetailsEventFragment newInstance(int mEventId) {
        DetailsEventFragment fragment = new DetailsEventFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, mEventId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mMeetupId = getArguments().getInt(ARG_PARAM1);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_details_meetups, container, false);
        myDatabaseHelper = new DatabaseHelper(getContext());
        TextView textViewMeetupTitle = view.findViewById(R.id.txtMeetupTitle);
        ImageView imageViewMeetup = view.findViewById(R.id.imageViewMeetup);
        ImageView imageViewClose = view.findViewById(R.id.imageViewClose);
        TextView textViewLeague = view.findViewById(R.id.textViewLeague);
        TextView textViewTeam = view.findViewById(R.id.textViewTeams);
        TextView textViewAddress = view.findViewById(R.id.textViewAddress);
        TextView textViewDate = view.findViewById(R.id.textViewDate);
        TextView textViewSeatsRemaining = view.findViewById(R.id.textViewSeatNumber);

        try {
            Event meetup = myDatabaseHelper.getEvent(mMeetupId);
            textViewMeetupTitle.setText(meetup.getName());
            Picasso.get().load(meetup.getImage()).into(imageViewMeetup);
            textViewLeague.setText("Playing \n League " + meetup.getTeam1().getLeague());
            textViewTeam.setText(meetup.getTeam1().getName().trim() + "\nVs\n" + meetup.getTeam2().getName());
            textViewAddress.setText(meetup.getAddress() + "\n" + meetup.getCity() + " , " + meetup.getProvince() + "\n" + meetup.getPostalCode());
            textViewDate.setText(String.format(meetup.getDate(), "dd-MM-yyy") + "\n" + meetup.getTime());
            int remaining = meetup.getCapacity() - myDatabaseHelper.getNumOfAttendants(meetup.getId());
            textViewSeatsRemaining.setText(String.valueOf(remaining));
            imageViewClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getSupportFragmentManager().beginTransaction().remove(DetailsEventFragment.this).commit();
                }
            });
        } catch (Exception ex) {
            Log.e("DetailsEvent", ex.getMessage());
        }
        return view;
    }

}
