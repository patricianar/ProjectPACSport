package com.example.projectpacsport;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class  MainActivity extends AppCompatActivity implements LeaguesFragment.OnFragmentInteractionListener {
    ArrayList<Result> listResults = new ArrayList<>();
    LeaguesFragment leaguesFragment;
    ResultsFragment resultsFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        leaguesFragment = new LeaguesFragment();
        resultsFragment = new ResultsFragment();

//        Bundle bundle = getIntent().getExtras();
//        listResults = (ArrayList<Result>) bundle.getSerializable("teamResults");

        VolleyService request = new VolleyService(this);
        String url = "https://api.mysportsfeeds.com/v2.1/pull/nba/current/date/20200208/games.json";
        request.executeRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);
                    JSONArray gamesArray = jsonObj.getJSONArray("games");
                    for (int i = 0; i < gamesArray.length(); i++) {
                        Team awayTeam = new Team();
                        Team homeTeam = new Team();
                        Result result = new Result();
                        JSONObject games = gamesArray.getJSONObject(i);
                        JSONObject schedule = games.getJSONObject("schedule");
                        JSONObject awayTeamObj = schedule.getJSONObject("awayTeam");
                        awayTeam.setId(awayTeamObj.getInt("id"));
                        JSONObject homeTeamObj = schedule.getJSONObject("homeTeam");
                        homeTeam.setId(homeTeamObj.getInt("id"));
                        JSONObject score = games.getJSONObject("score");
                        awayTeam.setScore(score.getInt("awayScoreTotal"));
                        homeTeam.setScore(score.getInt("homeScoreTotal"));
                        result.setAwayTeam(awayTeam);
                        result.setHomeTeam(homeTeam);
                        listResults.add(result);
                    }
                    JSONObject references = jsonObj.getJSONObject("references");
                    JSONArray teamRefArray = references.getJSONArray("teamReferences");
                    for (int i = 0; i < teamRefArray.length(); i++) {
                        JSONObject teamRef = teamRefArray.getJSONObject(i);
                        for (int j = 0; j < listResults.size(); j++) {
                            if (listResults.get(j).getAwayTeam().getId() == teamRef.getInt("id")) {
                                listResults.get(j).getAwayTeam().setName(teamRef.getString("name"));
                                listResults.get(j).getAwayTeam().setLogo(teamRef.getString("officialLogoImageSrc"));
                                Log.i("Test", listResults.get(j).getAwayTeam().getLogo());
                                break;
                            }
                            if (listResults.get(j).getHomeTeam().getId() == teamRef.getInt("id")) {
                                listResults.get(j).getHomeTeam().setName(teamRef.getString("name"));
                                listResults.get(j).getHomeTeam().setLogo(teamRef.getString("officialLogoImageSrc"));
                            }
                        }
                    }
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("teamResults", listResults);
                    resultsFragment.dataToDisplay(bundle);
                    // catch for the JSON parsing error
                } catch (JSONException ex) {
                    Log.e("JSON: ", ex.getMessage());
                }
            }
        });

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();

        transaction.add(R.id.fragmentLeagues, leaguesFragment, "LeaguesFragment");
        transaction.add(R.id.fragmentResults, resultsFragment, "ResultsFragment");
        transaction.commit();
    }

    @Override
    public void dataFromFragment(Bundle bundle) {
        resultsFragment.dataToDisplay(bundle);
    }
}




