package com.example.projectpacsport;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import java.util.ArrayList;

public class MyListEventsAdapter extends RecyclerView.Adapter<MyListEventsAdapter.ViewHolder> {
    private ArrayList<Event> listEvents;

    public MyListEventsAdapter(ArrayList<Event> listEvents) {
        this.listEvents = listEvents;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.event_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {
            final Context context = holder.tvEventName.getContext();
            final Event event = listEvents.get(position);

            //GlideToVectorYou.justLoadImage(mActivity, Uri.parse(result.getAwayTeam().getLogo()), holder.imgAwayTLogo);
            holder.tvEventName.setText(event.getTeam1() + " - " + event.getTeam2());
            holder.tvEventLocation.setText(event.getLocation());
            holder.tvEventCapacity.setText(event.getCapacity());

            holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    Intent intent = new Intent(context, GameActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("Event", event);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);
                }
            });
        }catch (Exception e){
            Log.e("Adapter:", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return listEvents.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventName, tvEventLocation, tvEventCapacity;
        LinearLayout relativeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            this.tvEventName = itemView.findViewById(R.id.tvEventName);
            this.tvEventLocation = itemView.findViewById(R.id.tvEventLocation);
            this.tvEventCapacity = itemView.findViewById(R.id.tvEventCapacity);
            relativeLayout =  itemView.findViewById(R.id.linearLayout);
        }
    }

}
