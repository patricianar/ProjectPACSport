package com.example.projectpacsport;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.io.IOException;

public class GameHighLightsFragment extends Fragment {
    private static final String DEFAULT_VIDEO_ID = "wlxDVJVk2qc"; // This one can be changed
    private static final long NUMBER_OF_VIDEOS_RETURNED = 1; // Take only 1 video
    private String youtubeAPIKey;
    private String videoId;

    public static GameHighLightsFragment newInstance() {
        return new GameHighLightsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_game_highlights, container, false);

        // Get arguments from Game Activity
        Bundle bundle = this.getArguments();
        Result result = (Result) bundle.getSerializable("Result");

        // Get awayTeam and homeTeam from the result
        Team awayTeam = result.getAwayTeam();
        Team homeTeam = result.getHomeTeam();

        // Create a search string
        String searchQuery = awayTeam.getName() + " " + homeTeam.getName() + " " + "highlight";

        // Create a service from com.google.api.services.youtube.YouTube
        // Duplicate code from the link below, file name Search.java
        // https://github.com/youtube/api-samples
        youtubeAPIKey = view.getContext().getString(R.string.youtube_api_key);
        YouTube mService = null;
        HttpTransport transport = new NetHttpTransport();
        JsonFactory jsonFactory = JacksonFactory.getDefaultInstance();
        mService = new YouTube.Builder(transport, jsonFactory, new HttpRequestInitializer() {
            public void initialize(HttpRequest request) throws IOException {
            }
        }).build();
        try {
            // Generate result list from the API
            YouTube.Search.List search = mService.search().list("id");

            // Set key, query and max result from the API
            search.setKey(youtubeAPIKey);
            search.setQ(searchQuery);
            search.setMaxResults(NUMBER_OF_VIDEOS_RETURNED);

            // Execute the search
            SearchListResponse response = search.execute();

            if (response.getItems().size() >= 1) {
                SearchResult searchResult = response.getItems().get(0);
                videoId = searchResult.getId().getVideoId();

                System.out.println("videoId" + videoId);
                System.out.println("videoId" + searchResult.toPrettyString());
            }

            // Initialize YouTubePlayerView from
            // https://github.com/PierfrancescoSoffritti/android-youtube-player
            YouTubePlayerView youTubePlayerView = view.findViewById(R.id.youtube_player_view);
            getLifecycle().addObserver(youTubePlayerView);
            youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
                @Override
                public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                    if (videoId == null || videoId.isEmpty()) {
                        videoId = DEFAULT_VIDEO_ID;
                    }

                    youTubePlayer.loadVideo(videoId, 0);
                }

                @Override
                public void onError(YouTubePlayer youTubePlayer, PlayerConstants.PlayerError error) {
                    System.out.println(error.toString());
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
