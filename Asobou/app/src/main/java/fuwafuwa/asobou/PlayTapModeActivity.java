package fuwafuwa.asobou;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Random;

import fuwafuwa.asobou.model.RetrieveLyricsFile;
import fuwafuwa.asobou.model.PostData;
import fuwafuwa.asobou.model.Song;
import fuwafuwa.asobou.model.User;


public class PlayTapModeActivity extends YouTubeFailureRecoveryActivity {

    private Song song;
    //private List<List<Integer>> timings = new ArrayList<>();
    private TextView lyricsTextView;
    private static final String TAG = "PlayTapModeActivity";
    private YouTubePlayer youTubePlayer;
    private int currTime;
    private int lineNum = 0;
    private int lastTiming = -1;
    private Button answer1;
    private Button answer2;
    private Button answer3;
    private Button answer4;
    private String[] missingWord = new String[3];
    private boolean done = false;
    private String currLine = "";
    private boolean firstRun = true;
    private int score = 0;

    private Handler hUpdate;
    private Runnable rUpdate;
    RetrieveLyricsFile lyrics = new RetrieveLyricsFile();

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

    private String blankLyrics(String lyrics) { // TODO: minimum of 3-4 characters for blank word
        // substring using "\n", then blank a word, then add "\n" back in, then do one big string
        if(lyrics.length() == 0) { return ""; }
        String[] linesAsArray = lyrics.split("\n");
        Arrays.copyOf(linesAsArray, linesAsArray.length - 1); // remove leftover empty object
        String blankLyrics = "";

        for (int i = 0; i < linesAsArray.length; i++) {
            String line = linesAsArray[i];
            if (!line.contains("_")) {

                // select random section to blank out
                int len = line.length();
                Random rand = new Random();
                int startIndex = rand.nextInt(len - 1);
                int endIndex = rand.nextInt(len - startIndex) + startIndex + 1;
                missingWord[i] = line.substring(startIndex, endIndex); // substring(regionStart, regionEnd)

                String[] incorrect = {new String(jumble(missingWord[0].toCharArray())),
                        new String(jumble(missingWord[0].toCharArray())),
                        new String(jumble(missingWord[0].toCharArray()))};

                int buttonNum = new Random().nextInt(4);
                if(buttonNum == 1) {
                    answer1.setText(missingWord[0]);
                    answer2.setText(incorrect[0]);
                    answer3.setText(incorrect[1]);
                    answer4.setText(incorrect[2]);
                } else if (buttonNum == 2) {
                    answer1.setText(incorrect[0]);
                    answer2.setText(missingWord[0]);
                    answer3.setText(incorrect[1]);
                    answer4.setText(incorrect[2]);
                } else if (buttonNum == 3) {
                    answer1.setText(incorrect[0]);
                    answer2.setText(incorrect[1]);
                    answer3.setText(missingWord[0]);
                    answer4.setText(incorrect[2]);
                } else {
                    answer1.setText(incorrect[0]);
                    answer2.setText(incorrect[1]);
                    answer3.setText(incorrect[2]);
                    answer4.setText(missingWord[0]);
                }

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

    private static char[] jumble(char[] arr){
        Random r = new Random();
        for(int i = arr.length-1; i > 0; i--){
            int rand = r.nextInt(i);
            char temp = arr[i];
            arr[i] = arr[rand];
            arr[rand] = temp;
        }
        return arr;
    }

    private void checkAnswer(String answer) { // TODO: regulate single answer check per line
        if (missingWord[0] != null) {
            if(answer.equals(missingWord[0])) {
                score += 100;
            } else {
                // incorrect answer UI alert
            }
        }
    }

    private void end() {
        done = true;

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String date = df.format(today);

        // THIS DOESN'T WORK
        /*String userId = "add_user_id=" + User.currentUser.getId();
        String songId = "add_song_id=" + song.getId();
        String temp = "add_score=" + (String.valueOf(score));
        char[] addscore = new char[temp.length()];
        for (int i = 0; i < temp.length(); i++) {
            addscore[i] = temp.charAt(i);
        }
        String adddate = "add_date=" + date; //date2015-08-18 12:00:00";
        String and = "&"; // pretty pointless, I know
        PostData testScore = new PostData(userId + and + songId + and + addscore + and + adddate);// http://198.199.94.36/change/backend/addscore.php
        testScore.execute("http://198.199.94.36/change/backend/addscore.php");*/

        // TODO: get dynamic score POST to work
        // TODO: check if 'edit' or 'add' POST is needed
        // THIS WORKS
        String userId = "add_user_id=" + User.currentUser.getId();
        String songId = "add_song_id=" + song.getId();
        String scoreToString = Integer.toString(score);
        String newScore = "add_score=" + scoreToString; // score won't push if passed an int
        String adddate = "add_date=" + date; //date2015-08-18 12:00:00";
        PostData testScore = new PostData(userId + "&" + songId + "&" + newScore + "&" + adddate);// http://198.199.94.36/change/backend/addscore.php
        testScore.execute("http://198.199.94.36/change/backend/addscore.php");

        //Log.d(TAG, " - currTime == song.getLength()");
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
