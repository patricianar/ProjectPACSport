package com.example.projectpacsport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class confirmationEvent extends AppCompatActivity {
    private static final String sharedPrefFile = "com.example.projectpacsport";
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor preferencesEditor;
    private Context mContext = confirmationEvent.this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_event);

        Bundle bundle = getIntent().getExtras();
        String eventName = bundle.getString("name");
        String eventCapacity = bundle.getString("capacity");
        String address = bundle.getString("address");
        String city = bundle.getString("City");
        String province = bundle.getString("province");
        String zipCode = bundle.getString("postalCode");
        String date = bundle.getString("date");
        String time = bundle.getString("time");
        String team1 = bundle.getString("team1Name");
        String team2 = bundle.getString("team2Name");
        String country = bundle.getString("country");
        String league = bundle.getString("league");
        String image = bundle.getString("image");
        ImageView imageViewPicture = findViewById(R.id.imageView3);

        Uri imageEvent = Uri.parse(image);

        TextView txtName = findViewById(R.id.txtEventName);
        txtName.setText(eventName);

        TextView txtDate = findViewById(R.id.txtDateOrder);
        txtDate.setText(date);

        TextView txtTime = findViewById(R.id.txtTimeOrder);
        txtTime.setText(time);

        TextView txtAddress = findViewById(R.id.addressEvent);
        txtAddress.setText(address);

        TextView txtCity = findViewById(R.id.txtCityEvent);
        txtCity.setText(city);

        TextView txtProvince = findViewById(R.id.txtProvinceEvent2);
        txtProvince.setText(province);

        TextView txtCountry = findViewById(R.id.countryEvent);
        txtCountry.setText(country);

        TextView txtTeam1 = findViewById(R.id.team1Order2);
        txtTeam1.setText(team1);

        TextView txtTeam2 = findViewById(R.id.team2Order2);
        txtTeam2.setText(team2);

        TextView txtCapacity = findViewById(R.id.capacity);
        txtCapacity.setText(eventCapacity);

        TextView txtLeague = findViewById(R.id.league);
        txtLeague.setText(league);

        try
        {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(),imageEvent);
            imageViewPicture.setImageBitmap(bitmap);
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }

    private void initBottomNavigationView() {
        //Initialize and Assign variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Result Selector
        bottomNavigationView.setSelectedItemId(R.id.create_event);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.results:
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        finish(); // avoid going back to the same selected tab many times - save memory
                        return true;
                    case R.id.create_event:
                        startActivity(new Intent(getApplicationContext(), RegisterEvents.class));
                        finish();
                        return true;
                    case R.id.search_event:
                        startActivity(new Intent(getApplicationContext(), EventsActivity.class));
                        finish();
                        return true;
                    case R.id.profile_menu:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        finish();
                        return true;
                }
                return false;
            }
        });
    }
}
