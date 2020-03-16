package com.example.projectpacsport;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class RegisterEvents extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapActivity";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;
    GoogleMap map;

    DatePickerDialog.OnDateSetListener dListener;
    DatabaseHelper myDatabaseHelper;
    private Boolean mLocationPermissionsGranted = false;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    //Widget Section
    private EditText mSearchText;
    private String addressMap = "";
    private String city = "";
    private String zipCode = "";
    private String province = "";
    private double latitude;
    private double longitude;
    private String country;
    private Spinner spinnerLeague, spinnerTeam1, spinnerTeam2;
    private ArrayList<String> spinnerData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_events);

        final ImageView imageLogo = findViewById(R.id.imgLogo);
        final EditText name = findViewById(R.id.editTextName);
        final EditText time = findViewById(R.id.editTextTime);
        final EditText duration = findViewById(R.id.editTextDuration);
        final EditText capacity = findViewById(R.id.editTextCapacity);
        final Button date = findViewById(R.id.btnDate);
        final Button submit = findViewById(R.id.btnSubmit);
        final Calendar today = Calendar.getInstance();
        final Calendar chosenDate = Calendar.getInstance();
        final DateFormat dateFormat = DateFormat.getDateInstance();
        final int numOfDays;


        mSearchText = findViewById(R.id.input_search);
        spinnerLeague = findViewById(R.id.spinnerLeague);
        spinnerTeam1 = findViewById(R.id.spinnerTeam1);
        spinnerTeam2 = findViewById(R.id.spinnerTeam2);
        myDatabaseHelper = new DatabaseHelper(RegisterEvents.this);


        spinnerData = myDatabaseHelper.getDataForSpinner(spinnerLeague.getSelectedItem().toString());
        final ArrayAdapter spinnerAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerData);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTeam1.setAdapter(spinnerAdapter);
        spinnerTeam2.setAdapter(spinnerAdapter);

        spinnerLeague.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {


            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                spinnerData = myDatabaseHelper.getDataForSpinner(spinnerLeague.getSelectedItem().toString());
                spinnerAdapter.clear();
                spinnerAdapter.addAll(spinnerData);


            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {


            }
        });

        getLocationPermission();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nameF = name.getText().toString();
                String timeF = time.getText().toString();
                String durationF = duration.getText().toString();
                String capacityF = capacity.getText().toString();
                String locationName = mSearchText.getText().toString();

                if (nameF.isEmpty()) {
                    Toast.makeText(RegisterEvents.this, "Please insert the name", Toast.LENGTH_SHORT).show();
                } else if (timeF.isEmpty()) {
                    Toast.makeText(RegisterEvents.this, "Please insert the time", Toast.LENGTH_SHORT).show();
                } else if (durationF.isEmpty()) {
                    Toast.makeText(RegisterEvents.this, "Please insert the duration", Toast.LENGTH_SHORT).show();
                } else if (capacityF.isEmpty()) {
                    Toast.makeText(RegisterEvents.this, "Please insert the capacity", Toast.LENGTH_SHORT).show();
                } else if (locationName.isEmpty()) {
                    Toast.makeText(RegisterEvents.this, "Please select a location using the map search bar", Toast.LENGTH_SHORT).show();
                }
            }
        });

        date.setOnClickListener(new View.OnClickListener() {
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
                            Toast.makeText(RegisterEvents.this, "Reservation has to be a future date", Toast.LENGTH_SHORT).show();
                        } else {
                            dateSelected = dateFormat.format(chosenDate.getTime());

                        }
                    }
                };
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterEvents.this, dListener, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Toast.makeText(this, "Map is Ready", Toast.LENGTH_SHORT).show();
        Log.d(TAG, "onMapReady: map is ready");
        map = googleMap;

        if (mLocationPermissionsGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(false);

            init();


        }
    }

    //private FusedLocationProviderClient mFusedLocationProviderClient; // Object or the library

    public void init() {
        Log.d(TAG, "init: initializing");
        mSearchText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                Log.d(TAG, "%%%%%%%%%%%IT IS INSIDE ");
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || keyEvent.getAction() == KeyEvent.ACTION_DOWN
                        || keyEvent.getAction() == KeyEvent.KEYCODE_ENTER) {

                    //execute our method for searching
                    geoLocate();
                }

                return false;
            }
        });
    }

    private void geoLocate() {
        Log.d(TAG, "geolocate: geolocating");
        String search = mSearchText.getText().toString();
        Geocoder geocoder = new Geocoder(RegisterEvents.this);
        List<Address> list = new ArrayList<>();
        mSearchText = findViewById(R.id.input_search);

        try {
            list = geocoder.getFromLocationName(search, 1);
        } catch (IOException e) {
            Log.d(TAG, "-------------IOExecption: --------------" + e.getMessage());
        }

        if (list.size() > 0) {
            Address address = list.get(0);
            Log.d(TAG, "!!!!!!!!!!!! Find location: !!!!!!!!!!" + address.toString());

            String[] MapResponse = address.toString().split(",");
            for (String a : MapResponse)
                Log.d(TAG, "---------------RESULT OF SPLIT---------" + a);

            String[] addressSplit = MapResponse[0].split("\"");
            for (String b : addressSplit)
                Log.d(TAG, "---------------RESULT OF SPLIT ADDRESS ---------" + b);

            addressMap = addressSplit[1]; //set address value
            Log.d(TAG, "**************** ADDRESS *****************" + addressMap);
            city = MapResponse[1]; //set city value
            Log.d(TAG, "**************** CITY *****************" + city);

            String[] zipCodeSplit = MapResponse[9].split("=");
            zipCode = zipCodeSplit[1]; // set zipcode
            Log.d(TAG, "**************** ZIPCODE *****************" + zipCode);

            String[] provinceSplit = MapResponse[5].split("=");
            province = provinceSplit[1]; // set province
            Log.d(TAG, "**************** PROVINCE *****************" + province);

            String[] latitudeSplit = MapResponse[13].split("=");
            latitude = Double.parseDouble(latitudeSplit[1]);  // set latitude
            Log.d(TAG, "**************** latitude *****************" + latitude);

            String[] longitudeSplit = MapResponse[15].split("=");
            longitude = Double.parseDouble(longitudeSplit[1]); // set longitude
            Log.d(TAG, "**************** longitude *****************" + longitude);

            String[] countrySplit = MapResponse[11].split("=");
            country = countrySplit[1]; // set country
            Log.d(TAG, "**************** COUNTRY *****************" + country);

            LatLng place = new LatLng(latitude, longitude);
            map.addMarker(new MarkerOptions().position(place).title(mSearchText.getText().toString()));
            map.moveCamera(CameraUpdateFactory.newLatLng(place));

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM);


        }
    }


    private void getLocationPermission() {
        Log.d(TAG, "getLocationPermission: getting location permissions");
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionsGranted = true;
                initMap();
            } else {
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void initMap() {
        Log.d(TAG, "initMap: initializing map");
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(RegisterEvents.this);
    }

    private void getDeviceLocation() {
        Log.d(TAG, "getDeviceLocation: getting the devices current location");

        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try {
            if (mLocationPermissionsGranted) {

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "onComplete: found location!");
                            Location currentLocation = (Location) task.getResult();

                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM);

                        } else {
                            Log.d(TAG, "onComplete: current location is null");
                            Toast.makeText(RegisterEvents.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e(TAG, "getDeviceLocation: SecurityException: " + e.getMessage());
        }
    }


    private void moveCamera(LatLng latLng, float zoom) {
        Log.d(TAG, "moveCamera: moving the camera to: lat: " + latLng.latitude + ", lng: " + latLng.longitude);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));


    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: called.");
        mLocationPermissionsGranted = false;

        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST_CODE: {
                if (grantResults.length > 0) {
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            mLocationPermissionsGranted = false;
                            Log.d(TAG, "onRequestPermissionsResult: permission failed");
                            return;
                        }
                    }
                    Log.d(TAG, "onRequestPermissionsResult: permission granted");
                    mLocationPermissionsGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }
}

