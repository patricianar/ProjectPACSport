package com.example.projectpacsport;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Date;

public class RegisterEvent extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_event);

        EditText name = findViewById(R.id.editTextEventName);
        EditText location = findViewById(R.id.editTextLocation);
        EditText address = findViewById(R.id.editTextAddress);
        EditText postalCode = findViewById(R.id.editTextPostalCode);
        EditText city = findViewById(R.id.editTextCity);
        EditText province = findViewById(R.id.editTextProvince);
        EditText country = findViewById(R.id.editTextCountry);
        EditText date = findViewById(R.id.editTextDate);
        EditText time = findViewById(R.id.editTextTime);
        EditText duration = findViewById(R.id.editTextDuration);
        EditText capacity = findViewById(R.id.editTextCapacity);
        MultiAutoCompleteTextView description = findViewById(R.id.multiLineDescription);
        Spinner team1 = findViewById(R.id.spinnerTeam1);
        Spinner team2 = findViewById(R.id.spinnerTeam2);
        Button submit = findViewById(R.id.btnSubmit);
    }
}
