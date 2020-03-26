package com.example.projectpacsport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

public class ProfileListAdapter extends ArrayAdapter<ProfileTaskItem> {
    private static final String TAG = "ProfileListAdapter";
    private Context mContext;
    private int mResource;

    public ProfileListAdapter(@NonNull Context context, int resource, @NonNull List<ProfileTaskItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Get the task information: each item is a ProfileTaskItem
        int imgResource = getItem(position).getImageResource();
        String taskName = getItem(position).getName();

        // Create the task item object
        LayoutInflater inflater = LayoutInflater.from(mContext);
        convertView = inflater.inflate(mResource, parent, false);

        // Find UI components and set values
        ImageView imageView = convertView.findViewById(R.id.img_profile_list_item);
        TextView txtTaskName = convertView.findViewById(R.id.txt_profile_list_item);

        imageView.setImageResource(imgResource);
        txtTaskName.setText(taskName);

        return convertView;
    }
}
