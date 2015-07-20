package fuwafuwa.asobou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.File;
import java.lang.reflect.Array;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import fuwafuwa.asobou.model.Song;

public class PlayTapModeActivity extends YouTubeFailureRecoveryActivity {

    private Song song;
    private static final String TAG = "PlayTapModeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_tap_mode);

        song = getIntent().getParcelableExtra("song");

        YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            player.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);

            // ridiculous sleep function
           /* try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/

            player.loadVideo(song.getYoutubeLink());
            //player.loadVideo("WIKqgE4BwAY"); // gimi choko 4:03
            Log.d(TAG, " - Video has finished loading.");
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    public void updateView(YouTubePlayer player, ArrayList<String[]> lyrics) {
        if(player.isPlaying()) {
            for(int line = 0; line < lyrics.size(); line++) {
                String[] currLine = lyrics.get(line);
                Time currentPlayerTime = new Time(player.getCurrentTimeMillis());
                if(currentPlayerTime.toString().equals(currLine[0])) {
                    scrollLyrics(currLine[1]);
                }
            }
        }
    }

    public void scrollLyrics(String lyrics) {
        // scroll lyrics into view
    }

    public String[] parseLyrics() {
        //File songLyrics = song.getLyricsKanji();
        List lines = new ArrayList<>();
        String[] lyrics = new String[0];
        return lyrics;
    }
}
