package fuwafuwa.asobou;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import fuwafuwa.asobou.model.RetrieveLyricsFile;
import fuwafuwa.asobou.model.Song;


public class PlayTypeModeActivity extends YouTubeFailureRecoveryActivity {

    private Song song;
    //private List<List<Integer>> timings = new ArrayList<>();
    private TextView lyricsTextView;
    private static final String TAG = "PlayTypeModeActivity";
    private YouTubePlayer youTubePlayer;
    private int currTime;
    private int lineNum = 0;
    private int lastTiming = -1;
    private EditText usrAnswer;
    private String[] missingWord = new String[3];
    private boolean done = false;
    private String currLine = "";
    private boolean firstRun = true;

    private Handler hUpdate;
    private Runnable rUpdate;
    RetrieveLyricsFile lyrics = new RetrieveLyricsFile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_type_mode);

        lyricsTextView = (TextView) findViewById(R.id.lyrics);
        usrAnswer = (EditText) findViewById(R.id.usrAnswer);

        song = getIntent().getParcelableExtra("song");
        lyrics.execute("http://198.199.94.36/lyrics/" + song.getLyricsKanji());

        final YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);

        // run update thread
        hUpdate = new Handler();
        rUpdate = new Runnable() {
            @Override
            public void run() {
                try {
                    currTime = youTubePlayer.getCurrentTimeMillis()/1000;
                    if(lyrics.newLine(currTime)) {
                        currLine = blankLyrics(lyrics.getLyrics(currTime));
                        lyricsTextView.setText(currLine);
                    }
                } catch (NullPointerException | IllegalStateException e) {
                   // e.printStackTrace();
                }
            }
        };
        Thread tUpdate = new Thread() {
            public void run() {
                while(true) {
                    try {
                        sleep(1);
                        if(currTime == song.getLength() && !done) { // if the video is done playing
                            end();
                            return;
                        }
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
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean wasRestored) {
        if (!wasRestored) {
            this.youTubePlayer = youTubePlayer;
            youTubePlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.CHROMELESS);
            youTubePlayer.loadVideo(song.getYoutubeLink());
            Log.d(TAG, " - Video has finished loading.");
        }
    }

    @Override
    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return (YouTubePlayerView) findViewById(R.id.youtube_view);
    }

    private String blankLyrics(String lyrics) {
        // substring using "\n", then blank a word, then add "\n" back in, then do one big string
        if(lyrics.length() == 0) { return ""; }
        String[] linesAsArray = lyrics.split("\n");
        Arrays.copyOf(linesAsArray, linesAsArray.length - 1); // remove leftover empty object
        String blankLyrics = "";

        for (int i = 0; i < linesAsArray.length; i++) {
            String line = linesAsArray[i];
            if (!line.contains("_")) {

                if(!firstRun) {
                    checkAnswer(usrAnswer.getText().toString());
                }

                // select random section to blank out
                int len = line.length();
                Random rand = new Random();
                int startIndex = rand.nextInt(len - 1);
                int endIndex = rand.nextInt(len - startIndex) + startIndex + 1;
                missingWord[i] = line.substring(startIndex, endIndex); // substring(regionStart, regionEnd)

                // dynamic blank size
                String blank = "";
                for(int j = 0; j < endIndex-startIndex; j++) {
                    blank += '_';
                }
                blankLyrics += line.substring(0, startIndex + 1) + blank + line.substring(endIndex, len);
                /*if(i != linesAsArray.length - 1) {
                    blankLyrics += "\n";
                }*/
            }
        }
        firstRun = false;
        return blankLyrics;
    }

    private void checkAnswer(String answer) { // TODO: 8/9/2015 scoring
        if (missingWord[0] != null) {
            if(answer.equals(missingWord[0])) {
                // yay you got it right
                Log.d(TAG, "Correct! " + answer + " = " + missingWord[0]);
                //return true;
            } else {
                // boo you suck
                Log.d(TAG, "You answered " + answer + "; Correct answer is " + missingWord[0]);
                //return false;
            }
        }
    }

    private void end() {
        done = true;
        checkAnswer(usrAnswer.getText().toString());
        Log.d(TAG, " - currTime == song.getLength()");
        youTubePlayer.setPlayerStateChangeListener(new YouTubePlayer.PlayerStateChangeListener() {
            @Override
            public void onLoading() {

            }

            @Override
            public void onLoaded(String s) {

            }

            @Override
            public void onAdStarted() {

            }

            @Override
            public void onVideoStarted() {

            }

            @Override
            public void onVideoEnded() {
                Log.d(TAG, " - video ended");
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayTypeModeActivity.this);
                builder.setMessage(R.string.song_end_dialog_message).setTitle(R.string.song_end_dialog_title);
                                /*builder.setPositiveButton(R.string.tap_mode, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startActivity(new Intent(PlayTapModeActivity.this, PlayTapModeActivity.class).putExtra("song", selectedSong));
                                    }
                                });*/
                builder.setNeutralButton(R.string.view_score, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(PlayTypeModeActivity.this, ScoreboardActivity.class));
                    }
                });
                builder.setNegativeButton(R.string.dashboard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(PlayTypeModeActivity.this, DashboardActivity.class));
                    }
                });

                AlertDialog dialog = builder.create();
                dialog.show();
            }

            @Override
            public void onError(YouTubePlayer.ErrorReason errorReason) {

            }
        });
    }
}
