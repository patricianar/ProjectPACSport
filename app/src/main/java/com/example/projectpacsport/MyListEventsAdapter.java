package com.example.projectpacsport;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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

import com.google.android.material.snackbar.Snackbar;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyListEventsAdapter extends RecyclerView.Adapter<MyListEventsAdapter.ViewHolder> {
    Activity activity;
    String[] months = {"Jan.", "Feb.", "Mar.", "Apr.", "May", "Jun", "Jul.", "Aug.", "Sept.", "Oct", "Nov", "Dec"};
    private DatabaseHelper myDatabaseHelper;
    private int currentUser;
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
        myDatabaseHelper = new DatabaseHelper(parent.getContext());
        SharedPreferences pref = parent.getContext().getSharedPreferences("SessionUser", Context.MODE_PRIVATE);
        currentUser = pref.getInt("UserId", 0);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        try {
            final Context context = holder.tvEventName.getContext();
            final Event event = listEvents.get(position);

            //review with time
//            Team team1 = myDatabaseHelper.getTeamsInfo(event.getTeam1Id());
//            Team team2 = myDatabaseHelper.getTeamsInfo(event.getTeam2Id());

//                  Uri imageEvent = Uri.parse(event.getImage());
//            context.grantUriPermission("com.example.projectpacsport", imageEvent, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION) ;
//
//            Log.e("aaaaa", imageEvent.toString());
//            try
//            {
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(),imageEvent);
//                holder.imageViewEvent.setImageBitmap(bitmap);
//            }catch(IOException e)
//            {
//                e.printStackTrace();
//            }


            Picasso.get().load(event.getImage()).into(holder.imageViewEvent);
            holder.tvEventName.setText(event.getName());
            String[] address = event.getAddress().split(",");
            holder.tvEventAddress.setText(address[0].trim());

//            int attendants = myDatabaseHelper.getNumOfAttendants(event.getId());
            final int capacity = event.getCapacity() - event.getAttendants();
            holder.tvEventCity.setText(event.getCity());
            String[] date = event.getDate().split("-");
            int monthNumb = Integer.parseInt(date[1]) - 1;
            holder.tvEventDate.setText(months[monthNumb] + " " + date[2]);
            holder.tvEventLeague.setText(event.getLeague());

            if (!event.isSelected()) {
                holder.imageViewCheck.setImageResource(R.drawable.ic_action_check_outline);
            } else {
                holder.imageViewCheck.setImageResource(R.drawable.ic_action_check);
            }
        } catch (Exception e) {
            Log.e("Adapter aaa:", e.getMessage());
        }
    }

    @Override
    public int getItemCount() {
        return listEvents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvEventName, tvEventAddress, tvEventCity, tvEventDate, tvEventLeague;
        ImageView imageViewCheck, imageViewEvent;
        ConstraintLayout constrainLayout;

        ViewHolder(View itemView) {
            super(itemView);
            this.tvEventName = itemView.findViewById(R.id.tvMeetupName);
            this.tvEventAddress = itemView.findViewById(R.id.tvMeetupAddress);
            this.tvEventCity = itemView.findViewById(R.id.tvMeetupCity);
            this.tvEventDate = itemView.findViewById(R.id.tvEventDate);
            this.tvEventLeague = itemView.findViewById(R.id.tvMeetupLeague);
            this.imageViewCheck = itemView.findViewById(R.id.imgViewCheck);
            this.imageViewEvent = itemView.findViewById(R.id.imageViewEvent);
            constrainLayout = itemView.findViewById(R.id.constrainLayout);

            try {

                imageViewCheck.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Event event = listEvents.get(getAdapterPosition());
//                    Intent intent = new Intent(context, GameActivity.class);
//                    Bundle bundle = new Bundle();
//                    bundle.putSerializable("Event", event);
//                    intent.putExtras(bundle);
//                    context.startActivity(intent);

                        //falta chequear si capacity 0!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
                        //hacer un proceso para esta queryy???????????????????????????????????????


                        if (!event.isSelected()) {
                            imageViewCheck.setImageResource(R.drawable.ic_action_check);
                            event.setSelected(true);
                            myDatabaseHelper.addEventAttendance(currentUser, event.getId());
//                            int capacity = event.getCapacity() - event.getAttendants();
//                            tvEventCapacity.setText("Capacity: " + capacity);
                            event.setAttendants(event.getAttendants() + 1);
                            Snackbar snackbar = Snackbar.make(constrainLayout, event.getName() + " added to my events", Snackbar.LENGTH_LONG);
                            snackbar.show();

                        } else {
                            imageViewCheck.setImageResource(R.drawable.ic_action_check_outline);
                            event.setSelected(false);
                            myDatabaseHelper.removeEventAttendance(currentUser, event.getId());
                            Snackbar snackbar = Snackbar.make(constrainLayout, event.getName() + " removed from from my events", Snackbar.LENGTH_LONG);
                            snackbar.show();
//                            int capacity = event.getCapacity() - event.getAttendants();
//                            tvEventCapacity.setText("Capacity: " + capacity);
                            event.setAttendants(event.getAttendants() - 1);

                        }
                    }
                });
                constrainLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Event event = listEvents.get(getAdapterPosition());
                        DetailsEventFragment detailsEventFragment = DetailsEventFragment.newInstance(event.getId());
                        AppCompatActivity activity = (AppCompatActivity) view.getContext();
                        activity.getSupportFragmentManager().beginTransaction().add(R.id.frameDetailsMeetup, detailsEventFragment).commit();
                    }
                });
            } catch (Exception e) {
                Log.e("Adapter:", e.getMessage());
            }
        }
    }

}

