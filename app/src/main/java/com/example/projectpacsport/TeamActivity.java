package com.example.projectpacsport;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TeamActivity extends AppCompatActivity{
    private static final String TAG = "TeamActivity";

    // SharedPreferences for the app widget
    private static final String sharedPrefFile = "com.example.projectpacsport";
    private SharedPreferences mPreferences;
    private SharedPreferences.Editor preferencesEditor;
    private Context mContext = TeamActivity.this;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        // UI Components
        ImageView imageTeam = findViewById(R.id.imgTeam);
        ToggleButton favoriteButton = findViewById(R.id.btn_favorite);


        // Data from bundle
        Bundle bundle = getIntent().getExtras();
        String myTeam = bundle.getString("team");
        String myLogo = bundle.getString("logo");
        final String myAbrebiation = bundle.getString("abre");
        final String league = bundle.getString("league");

        String url = "https://api.mysportsfeeds.com/v2.1/pull/" + league + "/2019-2020-regular/team_stats_totals.json?team="
                + myAbrebiation;

        Log.e("Stadium: ", url);

        // We call more info about team such as City and stadium name
        getTeamMoreInfo(url);
        GlideToVectorYou.justLoadImage(this, Uri.parse(myLogo), imageTeam);

        // Add and remove favorite team to SharedPreferences
        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        preferencesEditor = mPreferences.edit();

        // Set onCheckedChangeListener for the toggle button
        favoriteButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    Log.d(TAG, "onCheckedChanged: Added to Favorite");
                    preferencesEditor.putString("favoriteTeam", myAbrebiation);
                    preferencesEditor.putString("favoriteLeague", league);
                    preferencesEditor.apply();

                    Toast.makeText(mContext, "Your favorite team is added!", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d(TAG, "onCheckedChanged: Removed from Favorite");
                    preferencesEditor.remove("favoriteTeam");
                    preferencesEditor.remove("favoriteLeague");
                    preferencesEditor.apply();

                    Toast.makeText(mContext, "Your favorite team is removed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void getTeamMoreInfo(String url) {
        VolleyService request = new VolleyService(this);
        request.executeRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray teamStatsTotals = jsonObj.getJSONArray("teamStatsTotals"); //call parent objet
                    JSONObject teamMoreInfo = teamStatsTotals.getJSONObject(0);
                    JSONObject teamInformation = teamMoreInfo.getJSONObject("team"); //called team objet

                    String cityName = teamInformation.getString("city");
                    String teamName = teamInformation.getString("name");

                    TextView textView = findViewById(R.id.txtTeamName);
                    textView.setText(cityName + " " + teamName);

                    JSONObject stats = teamMoreInfo.getJSONObject("stats"); //called stats objet
                    //JSONObject statsInside = teamStatsTotals.getJSONObject(4);
                    JSONObject standing = stats.getJSONObject("standings"); //called team objet

                    String win = standing.getString("wins");
                    String losses = standing.getString("losses");
                    //String winPorcentage = standing.getString("winPct");
                    float winn;
                    float loss;
                    float winPorcentage;
                    float totalGames;

                    winn = Float.parseFloat(win);
                    loss = Float.parseFloat(losses);
                    totalGames = winn + loss;

                    winPorcentage = (winn / totalGames) * 100;
                    String winP = String.format("%.2f", winPorcentage);
                    // Log.e("winPorcentage: ", winPorcentage);
                    // Toast.makeText(TeamActivity.this, "Wins: " + win + " losses: " + losses + " W%: " + winPorcentage, Toast.LENGTH_SHORT).show();

                    TextView txtWins = findViewById(R.id.txtWinsNumber);
                    TextView txtLosses = findViewById(R.id.txtLossesNumber);
                    TextView txtWinPorcentage = findViewById(R.id.txtWinPercentageNumber);

                    txtWins.setText(win);
                    txtLosses.setText(losses);
                    txtWinPorcentage.setText(winP);

                    // txtWinPorcentage.setText(winPorcentage);

                    //call more
                    JSONObject homeVenue = teamInformation.getJSONObject("homeVenue");
                    String stadium = homeVenue.getString("name");
                    JSONArray socialMedia = teamInformation.getJSONArray("socialMediaAccounts");

                    JSONObject mediaType = socialMedia.getJSONObject(0);
                    String mediaT = mediaType.getString("mediaType");

                    JSONObject valueSocialMedia = socialMedia.getJSONObject(0);
                    String ValuemediaT = valueSocialMedia.getString("value");

                    TextView txtCity = findViewById(R.id.txtCityValue);
                    TextView txtStadium = findViewById(R.id.txtStadiumValue);
                    TextView txtSocialMedia = findViewById(R.id.txtMediaTypeValue);
                    TextView txtSocialMediaAccount = findViewById(R.id.txtSocialMediaValue);

                    txtCity.setText(cityName);
                    txtStadium.setText(stadium);
                    txtSocialMedia.setText(mediaT);
                    txtSocialMediaAccount.setText("@" + ValuemediaT);
                } catch (JSONException ex) {
                    Log.e("JSON: ", ex.getMessage());
                }

            }
        });
    }

}
