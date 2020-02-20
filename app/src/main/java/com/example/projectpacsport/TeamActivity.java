package com.example.projectpacsport;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class TeamActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_team);

        ImageView imageTeam = findViewById(R.id.imgTeam);

        Bundle bundle = getIntent().getExtras();
        String myTeam =  bundle.getString("team");
        String myLogo = bundle.getString("logo");
        String myAbrebiation = bundle.getString("abre");
        String league = bundle.getString("league");

        String url = "https://api.mysportsfeeds.com/v2.1/pull/" + league + "/2019-2020-regular/team_stats_totals.json?team="
                + myAbrebiation;

        Log.e("Stadium: ", url);

        getTeamMoreInfo(url); //we call more info about team such as City and stadium name

        GlideToVectorYou.justLoadImage(this, Uri.parse(myLogo), imageTeam);

    }

    public void getTeamMoreInfo(String url){
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

                    TextView textView = findViewById(R.id.TxtTeamName);
                    textView.setText(cityName + " " + teamName);

                    JSONObject stats = teamMoreInfo.getJSONObject("stats"); //called stats objet
                    //JSONObject statsInside = teamStatsTotals.getJSONObject(4);
                    JSONObject standing = stats.getJSONObject("standings"); //called team objet

                    String win = standing.getString("wins");
                    String losses = standing.getString("losses");
                    //String winPorcentage = standing.getString("winPct");

                   // Log.e("winPorcentage: ", winPorcentage);
                   // Toast.makeText(TeamActivity.this, "Wins: " + win + " losses: " + losses + " W%: " + winPorcentage, Toast.LENGTH_SHORT).show();

                    TextView txtWins = findViewById(R.id.txtWinNumber);
                    TextView txtLosses = findViewById(R.id.txtLossesNumber);
                    //TextView txtWinPorcentage = findViewById(R.id.txtWinPorcentageNumber);

                    txtWins.setText(win);
                    txtLosses.setText(losses);


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
                    TextView txtSocialMedia = findViewById(R.id.textView27txtMediaTypeValue);
                    TextView txtSocialMediaAccount = findViewById(R.id.txtSocialMediaValue);

                    txtCity.setText(cityName);
                    txtStadium.setText(stadium);
                    txtSocialMedia.setText(mediaT);
                    txtSocialMediaAccount.setText("@" + ValuemediaT);
                }
                catch (JSONException ex) {
                    Log.e("JSON: ", ex.getMessage());
                }

            }
        });
    }
}
