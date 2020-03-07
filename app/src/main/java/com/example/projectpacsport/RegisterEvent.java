package com.example.projectpacsport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;

public class RegisterEvent extends AppCompatActivity {
    DatePickerDialog.OnDateSetListener dListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_event);
        final EditText name = findViewById(R.id.editTextEventName);
        final EditText location = findViewById(R.id.editTextLocation);
        final EditText address = findViewById(R.id.editTextAddress);
        final EditText postalCode = findViewById(R.id.editTextPostalCode);
        final EditText city = findViewById(R.id.editTextCity);
        final EditText province = findViewById(R.id.editTextProvince);
        final EditText country = findViewById(R.id.editTextCountry);
        final Button btnDate = findViewById(R.id.btnDate);
        final EditText time = findViewById(R.id.editTextTime);
        final EditText duration = findViewById(R.id.editTextDuration);
        final EditText capacity = findViewById(R.id.editTextCapacity);
        final MultiAutoCompleteTextView description = findViewById(R.id.multiLineDescription);
        //Spinner team1 = findViewById(R.id.spinnerTeam1);
        //Spinner team2 = findViewById(R.id.spinnerTeam2);
       final Button submit = findViewById(R.id.btnSubmit);
      final   Calendar today = Calendar.getInstance();
       final Calendar chosenDate = Calendar.getInstance();
       final  DateFormat dateFormat = DateFormat.getDateInstance();

       final int numOfDays;


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameF = name.getText().toString();
                String locationF = location.getText().toString();
                String addressF = address.getText().toString();
                String postalCodeF = postalCode.getText().toString();
                String cityF = city.getText().toString();
                String provinceF = province.getText().toString();
                String countryF = country.getText().toString();
                String timeF = time.getText().toString();
                String durationF = duration.getText().toString();
                String capacityF = capacity.getText().toString();
                String descriptionF = description.getText().toString();


             //   do {
                        if(nameF.isEmpty())
                        {
                            Toast.makeText(RegisterEvent.this, "Please insert the name", Toast.LENGTH_SHORT).show();
                        }

                    else if(locationF.isEmpty())
                    {
                        Toast.makeText(RegisterEvent.this, "Please insert the location", Toast.LENGTH_SHORT).show();
                    }

                   else if(addressF.isEmpty())
                    {
                        Toast.makeText(RegisterEvent.this, "Please insert the address", Toast.LENGTH_SHORT).show();
                    }

                    else if(postalCodeF.isEmpty())
                    {
                        Toast.makeText(RegisterEvent.this, "Please insert the postal code", Toast.LENGTH_SHORT).show();
                    }

                   else  if(cityF.isEmpty())
                    {
                        Toast.makeText(RegisterEvent.this, "Please insert the city", Toast.LENGTH_SHORT).show();
                    }

                   else  if(provinceF.isEmpty())
                    {
                        Toast.makeText(RegisterEvent.this, "Please insert the province", Toast.LENGTH_SHORT).show();
                    }

                   else  if(countryF.isEmpty())
                    {
                        Toast.makeText(RegisterEvent.this, "Please insert the country", Toast.LENGTH_SHORT).show();
                    }

                    else if(timeF.isEmpty())
                    {
                        Toast.makeText(RegisterEvent.this, "Please insert the time", Toast.LENGTH_SHORT).show();
                    }

                    else if(durationF.isEmpty())
                    {
                        Toast.makeText(RegisterEvent.this, "Please insert the duration", Toast.LENGTH_SHORT).show();
                    }

                  else  if(capacityF.isEmpty())
                    {
                        Toast.makeText(RegisterEvent.this, "Please insert the capacity", Toast.LENGTH_SHORT).show();
                    }

                    else if(descriptionF.isEmpty())
                    {
                        Toast.makeText(RegisterEvent.this, "Please insert the description", Toast.LENGTH_SHORT).show();
                    }



                //    }while(!nameF.isEmpty() || !locationF.isEmpty() || !addressF.isEmpty() || !postalCodeF.isEmpty() || !cityF.isEmpty()
            //    || !provinceF.isEmpty() || !countryF.isEmpty() || !timeF.isEmpty() || !durationF.isEmpty() || !capacityF.isEmpty()
           //     || !descriptionF.isEmpty());

            }
        });

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dListener = new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String dateSelected;
                        chosenDate.set(Calendar.YEAR, year);
                        chosenDate.set(Calendar.MONTH, month);
                        chosenDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        if (chosenDate.compareTo(today) <= 0) {
                            Toast.makeText(RegisterEvent.this, "Reservation has to be a future date", Toast.LENGTH_SHORT).show();
                        } else {
                            dateSelected = dateFormat.format(chosenDate.getTime());

                        }
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterEvent.this, dListener, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

    }
}
