package fuwafuwa.asobou;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
    private String lyrics;
    private List<List<Integer>> timings = new ArrayList<>();
    private TextView lyricsTextView;
    private static final String TAG = "PlayTapModeActivity";
    private YouTubePlayer youTubePlayer;
    int currTime;
    int lineNum = 0;
    int lastTiming = -1;
    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;
    private String missingWord;
    private boolean done = false;

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
                checkAnswer(((Button)v).getText().toString());
            }
        });
        answer2 = (Button) findViewById(R.id.button2);
        answer2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(((Button)v).getText().toString());
            }
        });
        answer3 = (Button) findViewById(R.id.button3);
        answer3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(((Button)v).getText().toString());
            }
        });
        answer4 = (Button) findViewById(R.id.button4);
        answer4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAnswer(((Button)v).getText().toString());
            }
        });

        song = getIntent().getParcelableExtra("song");
        lyrics = song.getLyricsKanji();

        //Log.d(TAG, " - lyrics: " + lyrics);
        // timings.add(Array[timing, lyricsIndex]);
        List<Integer> one = new ArrayList<>();
        one.add(0); // at time 0 seconds
        one.add(0); // from index 0 to
        one.add(12); // 12 substring, including twelve
        timings.add(one);
        List<Integer> two = new ArrayList<>();
        two.add(10);
        two.add(13);
        two.add(25);
        timings.add(two);

        final YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        youTubeView.initialize(DeveloperKey.DEVELOPER_KEY, this);

        // run update thread
        hUpdate = new Handler();
        rUpdate = new Runnable() {
            @Override
            public void run() {
                try {
                    currTime = youTubePlayer.getCurrentTimeMillis()/1000;


                    if(lastTiming != currTime && lineNum < timings.size()) { // if the currTime matches the call time for the lyrics, set the textview once
                        if(timings.get(lineNum).contains(currTime)) {
                            Log.d(TAG, " - change lyrics at " + currTime);
                            lastTiming = currTime;
                            lyricsTextView.setText(blankLyrics()); //blankLyrics()
                        }
                    }

                } catch (NullPointerException | IllegalStateException e) {
                    e.printStackTrace();
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

    private String blankLyrics() {
        Log.d(TAG, "current line num = " + lineNum);
        int startTime = timings.get(lineNum).get(1);
        int endTime = timings.get(lineNum).get(2);
        Log.d(TAG, "" + startTime);
        Log.d(TAG, "" + endTime);
        lyrics =  "Hello world!! My name is N";
        String currLine = lyrics.substring(startTime, endTime);

        String[] wordsAsArray = currLine.split(" ");

        int index = new Random().nextInt(wordsAsArray.length);
        missingWord = wordsAsArray[index];

        int buttonNum = new Random().nextInt(4);
        if(buttonNum == 1) {
            answer1.setText(missingWord);
        } else if (buttonNum == 2) {
            answer2.setText(missingWord);
        } else if (buttonNum == 3) {
            answer3.setText(missingWord);
        } else {
            answer4.setText(missingWord);
        }

        // TODO: 8/9/2015 implement changing remaining selections to random/related words

        wordsAsArray[index] = "_______";
        String blankLyrics = "";
        for (int i = 0; i < wordsAsArray.length; i++) {
            blankLyrics = blankLyrics.concat(wordsAsArray[i]);
            if(i != wordsAsArray.length - 1) {
                blankLyrics = blankLyrics.concat(" ");
            }
        }
        lineNum++;
        return blankLyrics;
    }

    private void checkAnswer(String answer) { // TODO: 8/9/2015 scoring
        if (missingWord != null) {
            if(answer.equals(missingWord)) {
                // yay you got it right
                Log.d(TAG, "Correct! " + answer + " = " + missingWord);
                //return true;
            } else {
                // boo you suck
                Log.d(TAG, "You answered " + answer + "; Correct answer is " + missingWord);
                //return false;
            }
        }
    }

    private void end() {
        done = true;
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
                AlertDialog.Builder builder = new AlertDialog.Builder(PlayTapModeActivity.this);
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
                        startActivity(new Intent(PlayTapModeActivity.this, ScoreboardActivity.class));
                    }
                });
                builder.setNegativeButton(R.string.dashboard, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(PlayTapModeActivity.this, DashboardActivity.class));
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
