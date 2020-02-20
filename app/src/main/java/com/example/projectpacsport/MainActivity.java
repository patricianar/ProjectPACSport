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




