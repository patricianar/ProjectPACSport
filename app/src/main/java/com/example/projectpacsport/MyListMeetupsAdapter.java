package com.example.projectpacsport;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyListMeetupsAdapter extends RecyclerView.Adapter<MyListMeetupsAdapter.ViewHolder> {
    Activity activity;
    String[] months = {"Jan.", "Feb.", "Mar.", "Apr.", "May", "Jun", "Jul.", "Aug.", "Sept.", "Oct", "Nov", "Dec"};
    private DatabaseHelper myDatabaseHelper;
    private ArrayList<Event> listEvents;


    public MyListMeetupsAdapter(ArrayList<Event> listEvents) {
        this.listEvents = listEvents;
    }

    public ArrayList<Event> getData() {
        return listEvents;
    }

    public void removeItem(int position) {
        listEvents.remove(position);
        notifyItemRemoved(position);
    }

    public void restoreItem(Event event, int position) {
        listEvents.add(position, event);
        notifyItemInserted(position);
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem = layoutInflater.inflate(R.layout.meetup_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        myDatabaseHelper = new DatabaseHelper(parent.getContext());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {
            final Context context = holder.tvEventName.getContext();
            final Event event = listEvents.get(position);

            Picasso.get().load(event.getImage()).into(holder.imageViewEvent);
            holder.tvEventName.setText(event.getName());
            String[] address = event.getAddress().split(",");
            holder.tvEventAddress.setText(address[0].trim());

            final int capacity = event.getCapacity() - event.getAttendants();
            holder.tvEventCity.setText(event.getCity());
            String[] date = event.getDate().split("-");
            int monthNumb = Integer.parseInt(date[1]) - 1;
            holder.tvEventDate.setText(months[monthNumb] + " " + date[2]);
            holder.tvEventLeague.setText(event.getLeague());
            holder.tvEventRemaining.setText(String.valueOf(capacity));

        } catch (Exception e) {
            Log.e("Adapter :", e.getMessage() );
        }
    }

    @Override
    public int getItemCount() {
        return listEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventName, tvEventAddress, tvEventCity, tvEventDate, tvEventLeague, tvEventRemaining;
        ImageView imageViewEvent;
        ConstraintLayout constrainLayout;

        ViewHolder(View itemView) {
            super(itemView);
            this.tvEventName = itemView.findViewById(R.id.tvMeetupName);
            this.tvEventAddress = itemView.findViewById(R.id.tvMeetupAddress);
            this.tvEventCity = itemView.findViewById(R.id.tvMeetupCity);
            this.tvEventDate = itemView.findViewById(R.id.tvEventDate);
            this.tvEventLeague = itemView.findViewById(R.id.tvMeetupLeague);
            this.tvEventRemaining = itemView.findViewById(R.id.tvMeetupNumSeats);
            this.imageViewEvent = itemView.findViewById(R.id.imageViewEvent);
            constrainLayout = itemView.findViewById(R.id.constrainLayout);

            try {
                constrainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Event event = listEvents.get(getAdapterPosition());
                        DetailsEventFragment detailsEventFragment = DetailsEventFragment.newInstance(event.getId());
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().add(R.id.frameMyEvents, detailsEventFragment).commit();
                    }
                });
            } catch (Exception e) {
                Log.e("Adapter:", e.getMessage());
            }
        }
    }

}

