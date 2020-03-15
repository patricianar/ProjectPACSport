package com.example.projectpacsport;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyListPlayersAdapter extends RecyclerView.Adapter<MyListPlayersAdapter.ViewHolder> {
    private Activity mActivity;
    private ArrayList<Lineup> lineups;

    public MyListPlayersAdapter(Activity mActivity, ArrayList<Lineup> lineups) {
        this.mActivity = mActivity;
        this.lineups = lineups;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.player_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {
            final Context context = holder.tvAwayTPlayer.getContext();
            final Player awayTeamPlayer = lineups.get(position).getAway();
            final Player homeTeamPlayer = lineups.get(position).getHome();
            holder.tvAwayTPlayer.setText(awayTeamPlayer.getFirstName() + " " + awayTeamPlayer.getLastName());
            holder.tvHomeTPlayer.setText(homeTeamPlayer.getFirstName() + " " + homeTeamPlayer.getLastName());

        } catch (Exception e) {
            Log.e("Adapter:", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return lineups.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAwayTPlayer, tvHomeTPlayer;
        LinearLayout linearLayout;

        ViewHolder(final View itemView) {
            super(itemView);
            this.tvAwayTPlayer = itemView.findViewById(R.id.tvAwayTeamPlayer);
            this.tvHomeTPlayer = itemView.findViewById(R.id.tvHomeTeamPlayer);
            linearLayout = itemView.findViewById(R.id.linearLayout);
            final Intent intent = new Intent(itemView.getContext(), PlayerActivity.class);
            final Bundle bundle = new Bundle();

            this.tvAwayTPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("Player", tvAwayTPlayer.getText().toString());
                    // bundle.putString("League", );
                    intent.putExtras(bundle);
                    itemView.getContext().startActivity(intent);
                }
            });

            this.tvHomeTPlayer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    bundle.putString("Player", tvHomeTPlayer.getText().toString());
                    intent.putExtras(bundle);
                    itemView.getContext().startActivity(intent);
                }
            });
        }
    }

}
