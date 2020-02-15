package com.example.projectpacsport;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;


public class ResultsFragment extends Fragment {
    RecyclerView recyclerView;

    public ResultsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_results, container, false);
        recyclerView = view.findViewById(R.id.recyclerView);
        return view;
    }

    public void dataToDisplay(Bundle bundle) {
        ArrayList<Result> listResults = (ArrayList<Result>) bundle.getSerializable("teamResults");
        MyListResultsAdapter myAdapter = new MyListResultsAdapter(getActivity(), listResults);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(myAdapter);
    }
}
