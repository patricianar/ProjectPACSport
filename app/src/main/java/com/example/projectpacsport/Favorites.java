package com.example.projectpacsport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Favorites extends AppCompatActivity {
    ArrayList<Result> listResults = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites);

        final TextView tvNHL = findViewById(R.id.tvNHL);

        tvNHL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Favorites.this, MainActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("teamResults", listResults);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "https://api.mysportsfeeds.com/v2.1/pull/nhl/current/date/20200206/games.json";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
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
                                        Log.i("Test",  listResults.get(j).getAwayTeam().getLogo());
                                        break;
                                    }
                                    if (listResults.get(j).getHomeTeam().getId() == teamRef.getInt("id")) {
                                        listResults.get(j).getHomeTeam().setName(teamRef.getString("name"));
                                        listResults.get(j).getHomeTeam().setLogo(teamRef.getString("officialLogoImageSrc"));
                                    }
                                }
                            }
                            // catch for the JSON parsing error
                        } catch (JSONException ex) {
                            Log.e("JSON: ", ex.getMessage());
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                tvNHL.setText("That didn't work!");
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Content-Type", "application/json");
                String credentials = "5d63830e-ad82-459a-a7d4-6d1298:MYSPORTSFEEDS";
                String auth = "Basic " + Base64.encodeToString(credentials.getBytes(), Base64.NO_WRAP);
                params.put("Authorization", auth);
                return params;
            }
        };

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
