package com.example.projectpacsport;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {
    private SectionsPageAdapter mSectionPageAdapter;
    private ViewPager mViewPager;
    private ArrayList<Player> listPlayersAwayTeam = new ArrayList<>();
    private ArrayList<Player> listPlayersHomeTeam = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        Bundle bundle = getIntent().getExtras();
        Result result = (Result) bundle.getSerializable("Result");

        SharedPreferences sharedPref = getSharedPreferences("User", Context.MODE_PRIVATE);
        String league = sharedPref.getString("League", "nba");
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.clear();
        editor.commit();

        String url = "http://api.mysportsfeeds.com/v2.1/pull/" + league + "/current/games/20200208-"
                    + result.getAwayTeam().getAbbreviation() + "-" + result.getHomeTeam().getAbbreviation() + "/lineup.json";
Log.e("url", url);
        getPlayers(url);

        ImageView imgAwayTLogo = findViewById(R.id.imgAwayTeamLogo);
        ImageView imgHomeTLogo = findViewById(R.id.imgHomeTeamLogo);
        TextView tvAwayTName = findViewById(R.id.tvAwayTeamName);
        TextView tvHomeTName = findViewById(R.id.tvHomeTeamName);
        TextView tvAwayTScore = findViewById(R.id.tvAwayTeamScore);
        TextView tvHomeTScore = findViewById(R.id.tvHomeTeamScore);

        GlideToVectorYou.justLoadImage(this, Uri.parse(result.getAwayTeam().getLogo()), imgAwayTLogo);
        tvAwayTName.setText(result.getAwayTeam().getName());
        tvAwayTScore.setText(String.valueOf(result.getAwayTeam().getScore()));
        GlideToVectorYou.justLoadImage(this, Uri.parse(result.getHomeTeam().getLogo()), imgHomeTLogo);
        tvHomeTName.setText(result.getHomeTeam().getName());
        tvHomeTScore.setText(String.valueOf(result.getHomeTeam().getScore()));
    }

//    private void setupViewPager(ViewPager viewPager){
//        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
//        adapter.addFragment(new GameLineupFragment(), "TAB1");
//        adapter.addFragment(new GameHighLightsFragment(), "TAB2");
//        viewPager.setAdapter(adapter);
//    }


    public void getPlayers(String url) {
        VolleyService request = new VolleyService(this);
        request.executeRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {

                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray teamLineups = jsonObj.getJSONArray("teamLineups");
                    JSONObject awayTeam = teamLineups.getJSONObject(0);
                    JSONObject awayTeamLineups = awayTeam.getJSONObject("actual");
                    JSONArray awayTeamLineupsArray = awayTeamLineups.getJSONArray("lineupPositions");
                    for (int i = 0; i < awayTeamLineupsArray.length(); i++) {
                        Player awayTeamPlayer = new Player();
                        JSONObject playerObj = awayTeamLineupsArray.getJSONObject(i);
                        JSONObject player = playerObj.optJSONObject("player");
                        if (player != null) {
                            awayTeamPlayer.setFirstName(player.getString("firstName"));
                            awayTeamPlayer.setLastName(player.getString("lastName"));
                            listPlayersAwayTeam.add(awayTeamPlayer);
                        }
                    }
                    JSONObject homeTeam = teamLineups.getJSONObject(1);
                    JSONObject homeTeamLineups = homeTeam.getJSONObject("actual");
                    JSONArray homeTeamLineupsArray = homeTeamLineups.getJSONArray("lineupPositions");
                    for (int i = 0; i < homeTeamLineupsArray.length(); i++) {
                        Player homeTeamPlayer = new Player();
                        JSONObject playerObj = homeTeamLineupsArray.getJSONObject(i);
                        JSONObject player = playerObj.optJSONObject("player");
                        if (player != null) {
                            homeTeamPlayer.setFirstName(player.getString("firstName"));
                            homeTeamPlayer.setLastName(player.getString("lastName"));
                            listPlayersHomeTeam.add(homeTeamPlayer);
                        }
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("PlayersAway", listPlayersAwayTeam);
                    bundle.putSerializable("PlayersHome", listPlayersHomeTeam);

                    Fragment lineupFragment = GameLineupFragment.newInstance();
                    lineupFragment.setArguments(bundle);
                    mSectionPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
                    mViewPager = findViewById(R.id.view_pager);
                    mSectionPageAdapter.addFragment(lineupFragment, "LineUps");
                    mSectionPageAdapter.addFragment(new GameHighLightsFragment(), "HighLights");
                    mViewPager.setAdapter(mSectionPageAdapter);

                    TabLayout tabLayout = findViewById(R.id.tabs);
                    tabLayout.setupWithViewPager(mViewPager);

                    // catch for the JSON parsing error
                } catch (JSONException ex) {
                    Log.e("JSON: ", ex.getMessage());
                }
            }
        });
    }
}