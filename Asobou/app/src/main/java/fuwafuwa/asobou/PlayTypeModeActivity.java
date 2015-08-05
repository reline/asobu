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
import java.util.List;
import java.util.Random;

import fuwafuwa.asobou.model.Song;


public class PlayTypeModeActivity extends YouTubeFailureRecoveryActivity {

    private Song song;
    private List<String> lyrics;
    private List<Integer> timings = new ArrayList<>();
    TextView lyricsTextView;
    private static final String TAG = "PlayTypeModeActivity";
    YouTubePlayer youTubePlayer;
    int currTime;
    int lastTiming = -1;
    EditText usrAnswer;
    String missingWord;

    private Handler hUpdate;
    private Runnable rUpdate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_type_mode);

        lyricsTextView = (TextView) findViewById(R.id.lyrics);
        usrAnswer = (EditText) findViewById(R.id.usrAnswer);

        song = getIntent().getParcelableExtra("song");

        try { // to retrieve the song lyrics file as type ArrayList<String>
            lyrics = readFile(song.getLyricsKanji());
        } catch (IOException e) {
            e.printStackTrace();
            lyrics = new ArrayList<>();
            lyrics.add("hello world");
            lyrics.add("わたたたたた どっきゅん!");
            lyrics.add("ずきゅん! どきゅん!");
            lyrics.add("ずきゅん! どきゅん!");
            lyrics.add("ヤダ! ヤダ! ヤダ! ヤダ!");
        }
        Log.d(TAG, " - lyrics: " + lyrics);
        timings.add(4);
        timings.add(16);
        timings.add(28);
        timings.add(39);
        timings.add(50);
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
                    currTime = youTubePlayer.getCurrentTimeMillis()/1000;
                    //Log.d(TAG, " - " + currTime + "/" + song.getLength());
                    /*if(currTime < lyrics.size()) {
                        lyricsTextView.setText(blankLyrics());
                    }*/
                    if(lastTiming != currTime) { // if the currTime matches the call time for the lyrics, set the textview once
                        if(timings.contains(currTime)) {
                            Log.d(TAG, " - change lyrics at " + currTime);
                            lastTiming = currTime;
                            boolean correct = checkAnswer(usrAnswer.getText().toString());
                            lyricsTextView.setText(blankLyrics()); //blankLyrics()
                            Log.d(TAG, " - boolean correct = " + correct);
                        }
                    }
                    if(currTime == song.getLength()) { // if the video is done playing
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
                } catch (NullPointerException | IllegalStateException e) {
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
        String[] wordsAsArray = lyrics.get(timings.indexOf(currTime)).split(" ");

        int index = new Random().nextInt(wordsAsArray.length);
        missingWord = wordsAsArray[index];

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

    private boolean checkAnswer(String answer) {
        /*if (missingWord == null) {
            return; // do nothing if the first answer is not available
        }*/
        if(answer.equals(missingWord)) {
            // yay you got it right
            Log.d(TAG, "Correct! " + answer + " = " + missingWord);
            return true;
        } else {
            // boo you suck
            Log.d(TAG, "You answered " + answer + "; Correct answer is " + missingWord);
            return false;
        }
    }
}
