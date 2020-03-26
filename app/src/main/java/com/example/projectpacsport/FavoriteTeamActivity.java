package com.example.projectpacsport;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class FavoriteTeamActivity extends AppCompatActivity {
    private static final String TAG = "FavoriteTeamActivity";
    private Activity mActivity = FavoriteTeamActivity.this;

    // SharedPreferences for the app widget
    private static final String SHARED_PREF_FILE = "com.example.projectpacsport";
    private SharedPreferences mPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_team);

        // UI Components
        ImageView backButton = findViewById(R.id.back_arrow);

        // Get data from SharedPreferences
        mPreferences = getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
        String favTeamAbbre = mPreferences.getString("favoriteTeam", "");
        String favLeague = mPreferences.getString("favoriteLeague", "");

        System.out.println(favLeague);

        if (favTeamAbbre == null || favLeague == null) {
            Toast.makeText(mActivity, "You haven't add your favorite team yet!", Toast.LENGTH_LONG).show();
            finish();
        }
        String url = "https://api.mysportsfeeds.com/v2.1/pull/" + favLeague
                + "/2019-2020-regular/team_stats_totals.json?team=" + favTeamAbbre;

        // Get all information from the API
        getFavoriteTeamInfo(url);

        // Set back button listener
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void getFavoriteTeamInfo(String url) {
        VolleyService request = new VolleyService(this);
        request.executeRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    // UI Components
                    ImageView imageTeam = findViewById(R.id.img_fav_team);
                    TextView txtTeamName = findViewById(R.id.txt_fav_team_name);
                    TextView txtCity = findViewById(R.id.txt_fav_team_city_val);
                    TextView txtStadium = findViewById(R.id.txt_fav_team_stadium_val);
                    TextView txtSocialMediaType = findViewById(R.id.txt_fav_team_media_type_val);
                    TextView txtSocialMediaAccount = findViewById(R.id.txt_fav_team_account_val);
                    TextView txtWins = findViewById(R.id.txt_fav_team_wins_val);
                    TextView txtLosses = findViewById(R.id.txt_fav_team_losses_val);
                    TextView txtWinPercentage = findViewById(R.id.txt_fav_team_win_percent_val);

                    // Data from the API
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray teamStatsTotals = jsonObj.getJSONArray("teamStatsTotals");
                    JSONObject teamMoreInfo = teamStatsTotals.getJSONObject(0);
                    JSONObject teamInformation = teamMoreInfo.getJSONObject("team");

                    // Get City and Team Name, then set to txtTeamName
                    String cityName = teamInformation.getString("city");
                    String teamName = teamInformation.getString("name");
                    String imgUrl = teamInformation.getString("officialLogoImageSrc");
                    txtTeamName.setText(cityName + " " + teamName);
                    txtCity.setText(cityName);
                    GlideToVectorYou.justLoadImage(mActivity, Uri.parse(imgUrl), imageTeam);

                    // Get the Wins and Losses value
                    JSONObject stats = teamMoreInfo.getJSONObject("stats");
                    JSONObject standing = stats.getJSONObject("standings");
                    String win = standing.getString("wins");
                    String losses = standing.getString("losses");
                    String winPercentage = standing.getString("winPct");
                    txtWins.setText(win);
                    txtLosses.setText(losses);
                    txtWinPercentage.setText(winPercentage);

                    // Get the Home Venue and Social Media Account
                    JSONObject homeVenue = teamInformation.getJSONObject("homeVenue");
                    String stadium = homeVenue.getString("name");
                    txtStadium.setText(stadium);

                    // Get Social Media Account
                    JSONArray socialMediaArr = teamInformation.getJSONArray("socialMediaAccounts");
                    JSONObject socialMediaObj = socialMediaArr.getJSONObject(0);
                    String mediaType = socialMediaObj.getString("mediaType");
                    String mediaAccount = socialMediaObj.getString("value");
                    txtSocialMediaType.setText(mediaType);
                    txtSocialMediaAccount.setText("@" + mediaAccount);
                } catch (JSONException ex) {
                    Log.e("JSON: ", ex.getMessage());
                }
            }
        });
    }
}
