package fuwafuwa.asobou;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import java.util.Random;

import fuwafuwa.asobou.model.Song;

public class PlayTapModeActivity extends YouTubeFailureRecoveryActivity {

    private Song song;
    private List<String> lyrics;
    TextView lyricsTextView;
    private static final String TAG = "PlayTapModeActivity";
    YouTubePlayer player;
    int currTime;
    Button answer1;
    Button answer2;
    Button answer3;
    Button answer4;

    private Handler hUpdate;
    private Runnable rUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_tap_mode);

        lyricsTextView = (TextView) findViewById(R.id.lyrics);

        answer1 = (Button) findViewById(R.id.button);
        answer1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        answer2 = (Button) findViewById(R.id.button2);
        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        answer3 = (Button) findViewById(R.id.button3);
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        answer4 = (Button) findViewById(R.id.button4);
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

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
                try {
                    currTime = player.getCurrentTimeMillis()/1000;
                    Log.d(TAG, " - current time in secs: " + currTime);
                    if(currTime < lyrics.size()) {
                        lyricsTextView.setText(blankLyrics());
                    }
                } catch (NullPointerException e) {
                    e.printStackTrace();
                }
            }
        };
        Thread tUpdate = new Thread() {
            public void run() {
                while(true) {
                    try {
                        this.sleep(1);
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
            player.loadVideo(song.getYoutubeLink());
            Log.d(TAG, " - Video has finished loading.");
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
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

    private String blankLyrics() {
        String[] wordsAsArray = lyrics.get(currTime).split(" ");

        int index = new Random().nextInt(wordsAsArray.length);
        String randomWord = wordsAsArray[index];
        int buttonNum = new Random().nextInt(4);
        if(buttonNum == 1) {
            answer1.setText(randomWord);
        } else if (buttonNum == 2) {
            answer2.setText(randomWord);
        } else if (buttonNum == 3) {
            answer3.setText(randomWord);
        } else {
            answer4.setText(randomWord);
        }

        wordsAsArray[index] = "_______";
        String blankLyrics = "";
        for (int i = 0; i < wordsAsArray.length; i++) {
            blankLyrics = blankLyrics.concat(wordsAsArray[i]);
            if(i != wordsAsArray.length - 1) {
                blankLyrics = blankLyrics.concat(" ");
            }
        }
        return blankLyrics;
    }
}
