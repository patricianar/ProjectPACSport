package com.example.projectpacsport;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RemoteViews;
import android.widget.Spinner;
import android.widget.Switch;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.content.ContentValues.TAG;
import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class PACSportWidget extends AppWidgetProvider {
    private static final String TAG = "PACSportWidget";
    private static final String sharedPrefFile = "com.example.projectpacsport";
    static void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                final int appWidgetId) {

        // Extract favorite from SharedPreferences
        SharedPreferences mPreferences = context.getSharedPreferences(sharedPrefFile, MODE_PRIVATE);
        String favTeam = mPreferences.getString("favoriteTeam", "bos");
        String favLeague = mPreferences.getString("favoriteLeague", "nba");

        // Game data arrays
        final String[] awayTeam = new String[1];
        final String[] homeTeam = new String[1];
        final String[] awayScore = new String[1];
        final String[] homeScore = new String[1];
        final String url = "https://api.mysportsfeeds.com/v2.1/pull/" + favLeague + "/latest/games.json?team=" + favTeam;

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.pacsport_widget);

        // Get data from the API
        VolleyService request = new VolleyService(context);
        request.executeRequest(url, new VolleyService.VolleyCallback() {
            @Override
            public void getResponse(String response) {
                try {
                    JSONObject jsonObj = new JSONObject(response);

                    // Get the latest update date time
                    String dateTime = jsonObj.getString("lastUpdatedOn");
                    views.setTextViewText(R.id.widget_date_time,
                            context.getResources().getString(R.string.lastUpdatedOn) + " " + dateTime);

                    // Game and Team data
                    JSONArray gamesArr = jsonObj.getJSONArray("games");
                    JSONObject gamesObj = gamesArr.getJSONObject(0);
                    JSONObject scheduleObj = gamesObj.getJSONObject("schedule");
                    JSONObject awayTeamObj = scheduleObj.getJSONObject("awayTeam");
                    JSONObject homeTeamObj = scheduleObj.getJSONObject("homeTeam");
                    if (awayTeamObj != null) {
                        awayTeam[0] = awayTeamObj.getString("abbreviation");
                        Log.d(TAG, "awayTeamObj getResponse: " + awayTeam[0]);
                    }

                    if (homeTeamObj != null) {
                        homeTeam[0] = homeTeamObj.getString("abbreviation");
                        Log.d(TAG, "homeTeamObj getResponse: " + homeTeam[0]);
                    }

                    // Score data
                    JSONObject scoreObj = gamesObj.getJSONObject("score");
                    if (scoreObj != null) {
                        Log.d(TAG, "scoreObj getResponse: score");
                        awayScore[0] = scoreObj.getString("awayScoreTotal");
                        homeScore[0] = scoreObj.getString("homeScoreTotal");
                        views.setTextViewText(R.id.widget_away_score, awayScore[0]);
                        views.setTextViewText(R.id.widget_home_score, homeScore[0]);
                    }

                    // Logo and Team Name references
                    int indexAway = 0;
                    int indexHome = 0;
                    JSONObject references = jsonObj.getJSONObject("references");
                    JSONArray teamReferencesArr = references.getJSONArray("teamReferences");

                    while (teamReferencesArr.getJSONObject(indexAway).getString("abbreviation").compareToIgnoreCase(awayTeam[0]) != 0) {
                        indexAway++;
                    }

                    while (teamReferencesArr.getJSONObject(indexHome).getString("abbreviation").compareToIgnoreCase(homeTeam[0]) != 0) {
                        indexHome++;
                    }

                    JSONObject refAwayObj = teamReferencesArr.getJSONObject(indexAway);
                    JSONObject refHomeObj = teamReferencesArr.getJSONObject(indexHome);

                    String logoAwayUrl = refAwayObj.getString("officialLogoImageSrc");
                    String logoHomeUrl = refHomeObj.getString("officialLogoImageSrc");
                    String awayTeamName = refAwayObj.getString("name");
                    String homeTeamName = refHomeObj.getString("name");

                    System.out.println(logoAwayUrl);
                    System.out.println(logoHomeUrl);

                    views.setTextViewText(R.id.widget_away_team, awayTeamName + " " + awayTeam[0]);
                    views.setTextViewText(R.id.widget_home_team, homeTeamName + " " + homeTeam[0]);

                    // Todo: Add code here to display logos

                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views);

                } catch (JSONException ex) {
                    Log.e("JSON: ", ex.getMessage());
                }
            }
        });
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }
}

