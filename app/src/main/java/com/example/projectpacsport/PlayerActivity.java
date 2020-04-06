package com.example.projectpacsport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class PlayerActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "PlayerActivity";

    TextView tvPlayerName, tvPlayerBirthday, tvPlayerBirthPlace, tvPlayerHeight, tvPlayerWeight, tvPlayerTeam;
    ImageView imgPlayer, btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);

        // Initialize Components
        initializeComponents();

        // Get the bundle from Game Activity
        Bundle bundle = getIntent().getExtras();
        String playerName = bundle.getString("Player"); // set in MyListPlayersAdapter ViewHolder
        playerName = playerName.replace(' ', '-');

        // String league by sharedPref: now is the dummy league, Feb 18th, 2020
        SharedPreferences sharedPref = getSharedPreferences("User", 0);
        String league = sharedPref.getString("League", "nba");
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();

        // The string url for connecting to the api
        String url = "https://api.mysportsfeeds.com/v2.1/pull/" + league + "/players.json?player=" + playerName;

        // Get information of the selected player
        getPlayer(url);
    }

    private void getPlayer(String url) {
        final Player aPlayer = new Player();

        VolleyService request = new VolleyService(this);
        request.executeRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    Log.d(TAG, "getResponse: " + response);

                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray players = jsonObj.getJSONArray("players");
                    JSONObject playersObj = players.getJSONObject(0);
                    JSONObject player = playersObj.getJSONObject("player");

                    if (player != null) {
                        // information of the player
                        aPlayer.setId(player.getInt("id"));
                        aPlayer.setFirstName(player.getString("firstName"));
                        aPlayer.setLastName(player.getString("lastName"));
                        aPlayer.setBirthday(player.getString("birthDate"));
                        aPlayer.setBirthPlace(player.getString("birthCity"));
                        aPlayer.setHeight(player.getString("height"));
                        aPlayer.setWeight(player.getInt("weight"));
                        aPlayer.setImgSrc(player.getString("officialImageSrc").trim());
                    }

                    // Find current team name
                    JSONObject references = jsonObj.getJSONObject("references");
                    JSONArray teamReferences = references.getJSONArray("teamReferences");
                    JSONObject teamRefObject = teamReferences.getJSONObject(0);
                    aPlayer.setTeam(teamRefObject.getString("name"));

                    displayPlayer(aPlayer);

                } catch (JSONException ex) {
                    Log.e("JSON: ", ex.getMessage());
                }
            }
        });
    }

    private void displayPlayer(Player aPlayer) {
        Log.d(TAG, "displayPlayer: " + aPlayer.getImgSrc());

        // Display image of the player: this one may throw exception
        Picasso.get().load(aPlayer.getImgSrc()).into(imgPlayer);

        // Display information of the player
        tvPlayerName.setText(aPlayer.getFirstName() + " " + aPlayer.getLastName());
        tvPlayerBirthday.setText(aPlayer.getBirthday());
        tvPlayerBirthPlace.setText(aPlayer.getBirthPlace());
        tvPlayerHeight.setText(aPlayer.getHeight());
        tvPlayerWeight.setText(String.valueOf(aPlayer.getWeight()));
        tvPlayerTeam.setText(aPlayer.getTeam());
    }

    private void initializeComponents() {
        tvPlayerName = findViewById(R.id.txtViewDisplayPlayerName);
        tvPlayerBirthday = findViewById(R.id.txtViewDisplayBirthday);
        tvPlayerBirthPlace = findViewById(R.id.txtViewDisplayBirthPlace);
        tvPlayerHeight = findViewById(R.id.txtViewDisplayHeight);
        tvPlayerWeight = findViewById(R.id.txtViewDisplayWeight);
        tvPlayerTeam = findViewById(R.id.txtViewDisplayTeam);
        imgPlayer = findViewById(R.id.imgPlayer);
        btnBack = findViewById(R.id.backArrow);
        btnBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == btnBack) {
            finish();
        }
    }
}
