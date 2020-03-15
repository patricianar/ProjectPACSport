package com.example.projectpacsport;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class LeaguesFragment extends Fragment {
    private ArrayList<Result> listResults = new ArrayList<>();
    private OnFragmentInteractionListener mListener;
    String dateString;
    public LeaguesFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leagues, container, false);
        final ImageView imgNBA = view.findViewById(R.id.imgNBA);
        final ImageView imgNHL = view.findViewById(R.id.imgNHL);
        final ImageView imgMLB = view.findViewById(R.id.imgMLB);
        imgNHL.setAlpha(0.5f);
        imgMLB.setAlpha(0.5f);
        SharedPreferences sharedPref = view.getContext().getSharedPreferences("User", 0);
        final SharedPreferences.Editor editor = sharedPref.edit();

        //Getting yesterday's date for API requests
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar cal = Calendar.getInstance();
        cal.add(Calendar.DATE, -1); //get results day before
        dateString = dateFormat.format(cal.getTime());

        editor.putString("League", "nba");
        editor.commit();

        imgNBA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("League", "nba");
                editor.commit();
                listResults.clear();
                getResults("https://api.mysportsfeeds.com/v2.1/pull/nba/current/date/" + "20200308" + "/games.json");
                Log.e("test","https://api.mysportsfeeds.com/v2.1/pull/nba/current/date/" + "20200308" + "/games.json" );
                imgNBA.setAlpha(1f);
                imgNHL.setAlpha(0.5f);
                imgMLB.setAlpha(0.5f);
            }
        });
        imgNHL.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("League", "nhl");
                editor.commit();
                listResults.clear();
                getResults("http://api.mysportsfeeds.com/v2.1/pull/nhl/current/date/"+ "20200308" + "/games.json");
                Log.e("test", "http://api.mysportsfeeds.com/v2.1/pull/nhl/current/date/"+ "20200308" + "/games.json");
                imgNBA.setAlpha(0.5f);
                imgNHL.setAlpha(1f);
                imgMLB.setAlpha(0.5f);
            }
        });
        imgMLB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString("League", "mlb");
                editor.commit();
                listResults.clear();
                //getResults("https://api.mysportsfeeds.com/v2.1/pull/mlb/2019-2020-regular/date/20190506/games.json"); //have not started season
                imgNBA.setAlpha(0.5f);
                imgNHL.setAlpha(0.5f);
                imgMLB.setAlpha(1f);
            }
        });
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
            Calendar cal = Calendar.getInstance();
            cal.add(Calendar.DATE, -1);
            dateString = dateFormat.format(cal.getTime());
            getResults("https://api.mysportsfeeds.com/v2.1/pull/nba/current/date/" + "20200308" + "/games.json");
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
                        awayTeam.setAbbreviation(awayTeamObj.getString("abbreviation"));
                        JSONObject homeTeamObj = schedule.getJSONObject("homeTeam");
                        homeTeam.setId(homeTeamObj.getInt("id"));
                        homeTeam.setAbbreviation(homeTeamObj.getString("abbreviation"));
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
                    mListener.dataFromFragment(bundle); //sending the result to ResultFragment
                    // catch for the JSON parsing error
                } catch (JSONException ex) {
                    Log.e("JSON: ", ex.getMessage());
                } catch (Exception ex){
                    Log.e("Request: ", ex.getMessage());
                }
            }
        });
    }
}