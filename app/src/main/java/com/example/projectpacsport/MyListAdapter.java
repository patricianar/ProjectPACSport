package com.example.projectpacsport;

import android.app.Activity;
import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import java.util.ArrayList;

public class MyListAdapter extends RecyclerView.Adapter<MyListAdapter.ViewHolder> {
    private Activity mActivity;
    private ArrayList<Result> listResults;

    public MyListAdapter(Activity mActivity, ArrayList<Result> listResults) {
        this.mActivity = mActivity;
        this.listResults = listResults;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.list_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {
            Context context = holder.tvAwayTName.getContext();
            final Result result = listResults.get(position);
            GlideToVectorYou.justLoadImage(mActivity, Uri.parse(result.getAwayTeam().getLogo()), holder.imgAwayTLogo);
            holder.tvAwayTName.setText(result.getAwayTeam().getName());
            holder.tvAwayTScore.setText(String.valueOf(result.getAwayTeam().getScore()));
            GlideToVectorYou.justLoadImage(mActivity, Uri.parse(result.getHomeTeam().getLogo()), holder.imgHomeTLogo);
            holder.tvHomeTName.setText(result.getHomeTeam().getName());
            holder.tvHomeTScore.setText(String.valueOf(result.getHomeTeam().getScore()));
        }catch (Exception e){
            Log.e("Adapter:", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return listResults.size();
    }

    public void removeItem(int position) {
        listResults.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Result item, int position) {
        listResults.add(position, item);
        notifyItemInserted(position);
    }

    public ArrayList<Result> getData() {
        return listResults;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgAwayTLogo, imgHomeTLogo;
        TextView tvAwayTName, tvHomeTName, tvAwayTScore, tvHomeTScore;
        LinearLayout relativeLayout;


        ViewHolder(View itemView) {
            super(itemView);
            this.imgAwayTLogo =  itemView.findViewById(R.id.imgAwayTeamLogo);
            this.imgHomeTLogo = itemView.findViewById(R.id.imgHomeTeamLogo);
            this.tvAwayTName = itemView.findViewById(R.id.tvAwayTeamName);
            this.tvHomeTName = itemView.findViewById(R.id.tvHomeTeamName);
            this.tvAwayTScore = itemView.findViewById(R.id.tvAwayTeamScore);
            this.tvHomeTScore = itemView.findViewById(R.id.tvHomeTeamScore);
            relativeLayout =  itemView.findViewById(R.id.relativeLayout);

        }
    }

}
