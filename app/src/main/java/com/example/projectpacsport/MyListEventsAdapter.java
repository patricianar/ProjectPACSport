package com.example.projectpacsport;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyListEventsAdapter extends RecyclerView.Adapter<MyListEventsAdapter.ViewHolder> {
    private ArrayList<Event> listEvents;
    DatabaseHelper myDatabaseHelper;

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
            myDatabaseHelper = new DatabaseHelper(context);
            Team team1 = myDatabaseHelper.getTeamsInfo(event.getTeam1Id());
            Team team2 = myDatabaseHelper.getTeamsInfo(event.getTeam2Id());
            String[] months = {"Jan.", "Feb.", "Mar.", "Apr.", "May", "Jun", "Jul.", "Aug.", "Sept.", "Oct", "Nov", "Dec"};
            //GlideToVectorYou.justLoadImage(mActivity, Uri.parse(result.getAwayTeam().getLogo()), holder.imgAwayTLogo);
            holder.tvEventName.setText(event.getName() + " (" + team1.getAbbreviation() + " - " + team2.getAbbreviation() + " )");
            String[] address = event.getAddress().split(",");
            holder.tvEventAddress.setText(address[0].trim());
            holder.tvEventCapacity.setText("Capacity: " + String.valueOf(event.getCapacity()));
            String[] date = event.getDate().split("-");
            int monthNumb = Integer.parseInt(date[1]) - 1;
            holder.tvEventDate.setText(months[monthNumb] + " " + date[2]);
            holder.tvEventLeague.setText(team1.getLeague());

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
        TextView tvEventName, tvEventAddress, tvEventCapacity, tvEventDate, tvEventLeague;
        LinearLayout relativeLayout;

        ViewHolder(View itemView) {
            super(itemView);
            this.tvEventName = itemView.findViewById(R.id.tvEventName);
            this.tvEventAddress = itemView.findViewById(R.id.tvEventAddress);
            this.tvEventCapacity = itemView.findViewById(R.id.tvEventCapacity);
            this.tvEventDate = itemView.findViewById(R.id.tvEventDate);
            this.tvEventLeague = itemView.findViewById(R.id.tvEventLeague);
            relativeLayout =  itemView.findViewById(R.id.linearLayout);
        }
    }
}
