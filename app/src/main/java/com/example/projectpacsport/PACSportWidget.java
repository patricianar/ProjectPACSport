package com.example.projectpacsport;

import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.util.Log;
import android.widget.RemoteViews;

import com.caverock.androidsvg.SVG;
import com.caverock.androidsvg.SVGParseException;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;

/**
 * Implementation of App Widget functionality.
 */
public class PACSportWidget extends AppWidgetProvider {
    private static final String TAG = "PACSportWidget";
    private static final String SHARED_PREF_FILE = "com.example.projectpacsport";

    private void updateAppWidget(final Context context, final AppWidgetManager appWidgetManager,
                                 final int appWidgetId) {

        // Extract favorite from SharedPreferences
        SharedPreferences mPreferences = context.getSharedPreferences(SHARED_PREF_FILE, MODE_PRIVATE);
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

                    // Create a new thread to download image and load to view
                    Map<Integer, String> downloadItems = new HashMap<>();
                    downloadItems.put(R.id.widget_away_logo, logoAwayUrl);
                    downloadItems.put(R.id.widget_home_logo, logoHomeUrl);
                    ImageDownloader downloader = new ImageDownloader(views, appWidgetManager, appWidgetId, downloadItems);
                    new Thread(downloader).start();

                    views.setTextViewText(R.id.widget_away_team, awayTeamName + " " + awayTeam[0]);
                    views.setTextViewText(R.id.widget_home_team, homeTeamName + " " + homeTeam[0]);

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

    public class ImageDownloader implements Runnable {
        final RemoteViews views;
        final AppWidgetManager appWidgetManager;
        final int appWidgetId;
        Map<Integer, String> downloadImages;

        public ImageDownloader(RemoteViews views, AppWidgetManager appWidgetManager, int appWidgetId, Map<Integer, String> downloadImages) {
            this.views = views;
            this.appWidgetManager = appWidgetManager;
            this.appWidgetId = appWidgetId;
            this.downloadImages = downloadImages;
        }

        @Override
        public void run() {
            try {
                for (Map.Entry<Integer, String> entry : downloadImages.entrySet()) {
                    Log.d(TAG, "run: download image " + entry.getValue());
                    URL imageUrl = new URL(entry.getValue());

                    // Download SVG String from the url
                    HttpURLConnection conn = (HttpURLConnection) imageUrl.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

                    // A SVG file : a Scalable Vector Graphics file.
                    // Files in this format use an XML-based text format to describe how the image should appear.
                    // Read more: https://www.lifewire.com/svg-file-4120603
                    String inputLine;
                    StringBuilder sb = new StringBuilder();
                    inputLine = bufferedReader.readLine();
                    while (inputLine != null) {
                        sb.append(inputLine);
                        inputLine = bufferedReader.readLine();
                    }

                    // Convert from string in string builder to svg format
                    Log.d(TAG, "run: " + sb.toString());
                    SVG svg = SVG.getFromString(sb.toString());

                    // Convert svg to bitmap
                    // Step 1: create a bitmap
                    int svgWidth = 400;
                    int svgHeight = 400;
                    Bitmap bitmap = Bitmap.createBitmap(svgWidth, svgHeight, Bitmap.Config.ARGB_8888);

                    // Step 2: create a canvas
                    Canvas bmCanvas = new Canvas(bitmap);
                    bmCanvas.drawRGB(255, 255, 255); // clear background to white

                    // Step 3: render svg file to the canvas
                    svg.renderToCanvas(bmCanvas);

                    // Set Bitmap with view Id
                    views.setImageViewBitmap(entry.getKey(), bitmap);

                    // Instruct the widget manager to update the widget
                    appWidgetManager.updateAppWidget(appWidgetId, views);
                    Log.d(TAG, "run: end " + entry.getValue());
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (SVGParseException e) {
                e.printStackTrace();
            }
        }
    }
}

