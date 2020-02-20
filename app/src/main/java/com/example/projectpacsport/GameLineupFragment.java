package com.example.projectpacsport;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class GameLineupFragment extends Fragment {
    private ArrayList<Player> listPlayersAwayTeam = new ArrayList<>();
    private ArrayList<Player> listPlayersHomeTeam = new ArrayList<>();

    public static GameLineupFragment newInstance() {
        return new GameLineupFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_lineup, container, false);
        Bundle bundle = getArguments();

        listPlayersAwayTeam = (ArrayList<Player>) bundle.getSerializable("PlayersAway");
        listPlayersHomeTeam = (ArrayList<Player>) bundle.getSerializable("PlayersHome");

        MyListPlayersAdapter myAdapter = new MyListPlayersAdapter(getActivity(), listPlayersAwayTeam, listPlayersHomeTeam);
        RecyclerView recyclerView = view.findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(myAdapter);
        return view;
    }
}
