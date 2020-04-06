package com.example.projectpacsport;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;

public class ProfileActivity extends AppCompatActivity {
    User currentUser = new User();;
    String userId;
    String name;
    String lastName;
    String email;
    String image;
    String favoriteTeam;
    private Uri filePath;
    private final int PICK_IMAGE_REQUEST=71;
    private ImageView imageView;
    private String TAG="MyApp";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();

        TextView title = findViewById(R.id.txtProfileTitle);
        TextView nameValue = findViewById(R.id.txtProfileNameValue);
        TextView lastNameValue = findViewById(R.id.txtProfileLastNameValue);
        TextView emailValue = findViewById(R.id.txtProfileEmailValue);
        TextView favoriteValue = findViewById(R.id.txtProfileFavoriteTeamValue);
        Button addPhoto = findViewById(R.id.btnAddPhoto);
       /* editor.putInt("UserId", currentUser.getId());
        editor.putString("UserName", currentUser.getName());
        editor.putString("UserLastName", currentUser.getLastname());
        editor.putString("UserEmail", currentUser.getEmail());
        editor.commit(); */


            imageView = findViewById(R.id.imageView);

            //llamar image from sharedpreference
            image = pref.getString("image", image);
            Uri myUri = Uri.parse(image);
            myUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
            try
            {
                Bitmap bitmap2 = MediaStore.Images.Media.getBitmap(getContentResolver(), myUri);
                imageView.setImageBitmap(bitmap2);
            } catch (IOException e)
            {
                e.printStackTrace();
            }



        name = pref.getString("UserName", name);
        lastName = pref.getString("UserLastName", lastName);
        email = pref.getString("UserEmail", email);

        nameValue.setText(name);
        lastNameValue.setText(lastName);
        emailValue.setText(email);

        Log.d("UserProfile", "THE USER IS: " + name + " " + lastName + ", email: " + email);

        addPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });
        // Initialize BottomNavigationView
        initBottomNavigationView();
    }

    public void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.addFlags(Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        SharedPreferences pref = getApplicationContext().getSharedPreferences("SessionUser", MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null)
        {
            filePath = data.getData();
            editor.putString("image", filePath.toString());
            editor.commit();

            Log.d(TAG, "**************** filePath *****************" + filePath.toString());
            try
            {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                imageView.setImageBitmap(bitmap);
            } catch (IOException e)
            {
                e.printStackTrace();
            }
        }

        /**
         * Init BottomNavigationView with 4 items:
         * results, create event, search event, profile menu
         */
    }
        private void initBottomNavigationView () {
            //Initialize and Assign variable
            BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

            //Set Result Selector
            bottomNavigationView.setSelectedItemId(R.id.profile_menu);

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
