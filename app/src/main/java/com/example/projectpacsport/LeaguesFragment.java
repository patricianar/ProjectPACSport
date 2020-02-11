package com.example.projectpacsport;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

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
import java.util.HashMap;
import java.util.Map;


public class LeaguesFragment extends Fragment {
    private ArrayList<Result> listResults = new ArrayList<>();
    private OnFragmentInteractionListener mListener;

    public LeaguesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leagues, container, false);
        final ImageView imgNBA = view.findViewById(R.id.imgNBA);
        ImageView imgNFL = view.findViewById(R.id.imgNFL);
        ImageView imgNHL = view.findViewById(R.id.imgNHL);
        ImageView imgMLB = view.findViewById(R.id.imgMLB);
        imgNBA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listResults.clear();
                getResults("https://api.mysportsfeeds.com/v2.1/pull/nba/current/date/20200208/games.json");
                //imgNBA.setImageResource(R.drawable.logo);
            }
        });
        imgNFL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listResults.clear();
                getResults("https://api.mysportsfeeds.com/v2.1/pull/nfl/latest/date/20200202/games.json");
                //imgNBA.setImageResource(R.drawable.logo);
            }
        });
        imgNHL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listResults.clear();
                getResults("http://api.mysportsfeeds.com/v2.1/pull/nhl/current/date/20200208/games.json");
                // imgNBA.setImageResource(R.drawable.logo);
            }
        });

        return view;
    }

//    public void onButtonPressed(Uri uri) {
//        if (mListener != null) {
//            mListener.onFragmentInteraction(uri);
//        }
//    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        void dataFromFragment(Bundle bundle);
    }

    public void getResults(String url) {
        VolleyService request = new VolleyService(getContext());
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
                    mListener.dataFromFragment(bundle);
                    // catch for the JSON parsing error
                } catch (JSONException ex) {
                    Log.e("JSON: ", ex.getMessage());
                }
            }
        });
    }
}