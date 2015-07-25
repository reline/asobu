package fuwafuwa.asobou;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.Buffer;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import fuwafuwa.asobou.model.Song;

public class PlayTapModeActivity extends YouTubeFailureRecoveryActivity {

    private Song song;
    private List<String> lyrics;
    TextView lyricsTextView;
    private static final String TAG = "PlayTapModeActivity";
    YouTubePlayer player;

    private Handler hUpdate;
    private Runnable rUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_tap_mode);

        lyricsTextView = (TextView) findViewById(R.id.lyrics);

        song = getIntent().getParcelableExtra("song");



        try { // to retrieve the song lyrics file as type ArrayList<String>
            lyrics = readFile(song.getLyricsKanji());
        } catch (IOException e) {
            e.printStackTrace();
            lyrics = new ArrayList<>();
            lyrics.add("あたたたたた ずっきゅん!");
            lyrics.add("わたたたたた どっきゅん!");
            lyrics.add("ずきゅん! どきゅん!");
            lyrics.add("ずきゅん! どきゅん!");
            lyrics.add("ヤダ! ヤダ! ヤダ! ヤダ!");
        }
        Log.d(TAG, " - lyrics: " + lyrics);
         /*
            あたたたたた ずっきゅん!
            わたたたたた どっきゅん!
            ずきゅん! どきゅん!
            ずきゅん! どきゅん!
            ヤダ! ヤダ! ヤダ! ヤダ!
         */

        final YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);

        // run update thread
        hUpdate = new Handler();
        rUpdate = new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, " - current time in secs: " + player.getCurrentTimeMillis()/1000);
                if(player.getCurrentTimeMillis()/1000 < lyrics.size()) {
                    lyricsTextView.setText(lyrics.get(player.getCurrentTimeMillis() / 1000));
                }
            }
        };
        Thread tUpdate = new Thread() {
            public void run() {
                while(true) {
                    try {
                        this.sleep(25); // 40th of a second
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    hUpdate.post(rUpdate);
                }
            }
        };
        tUpdate.start();
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer player, boolean wasRestored) {
        if (!wasRestored) {
            this.player = player;
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

    public void updateView() {
        if(player.isPlaying()) {
            /*for(int line = 0; line < lyrics.size(); line++) {
                String currLine = lyrics.get(line);
                Time currentPlayerTime = new Time(player.getCurrentTimeMillis());
                if(currentPlayerTime.toString().equals(currLine)) {
                    scrollLyrics(currLine);
                }
            }*/
            int currTime = player.getCurrentTimeMillis()/1000;
            Log.d(TAG, " - Current video time in seconds: " + currTime);
            if(currTime <= lyrics.size()) {
                lyricsTextView.setText(lyrics.get(currTime));
            }
        }
    }

    public void scrollLyrics(String lyrics) {
        // scroll lyrics into view
        lyricsTextView.setMovementMethod(new ScrollingMovementMethod());
    }

    private List<String> readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line;
        //List<String> lyrics = new ArrayList<>();
        //StringBuilder stringBuilder= new StringBuilder();
        //String ls = System.getProperty("line.separator");

        while((line = reader.readLine()) != null) {
            //stringBuilder.append(line);
            //stringBuilder.append(ls);
            lyrics.add(line);
        }
        //return stringBuilder.toString();
        return lyrics;
    }
}
