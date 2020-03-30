package com.example.projectpacsport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;

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
    }
}
