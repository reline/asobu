package fuwafuwa.asobou;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

import fuwafuwa.asobou.model.Song;

public class PlayTapModeActivity extends YouTubeFailureRecoveryActivity {

    private Song song;
    private static final String TAG = "PlayTapModeActivity";
    ArrayList<String[]> lyrics = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_tap_mode);

        song = (Song) getIntent().getSerializableExtra("song");

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_play_tap_mode, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void onSettingsButtonClick(MenuItem menuItem) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void onHelpButtonClick(MenuItem menuItem) {
        startActivity(new Intent(this, HelpActivity.class));
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



}
