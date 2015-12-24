package fuwafuwa.asobou.activities;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fuwafuwa.asobou.R;
import fuwafuwa.asobou.model.JSONArrayToSongList;
import fuwafuwa.asobou.model.Song;
import fuwafuwa.asobou.webservices.GetSong;

public class ScoreboardActivity extends AppCompatActivity {

    private static final String TAG = "ScoreboardActivity";

    private List<Song> songList = new ArrayList<>();
    private ListView songListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        songListView = (ListView) findViewById(R.id.scoreboard_listview);
        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song selectedSong = (Song) parent.getAdapter().getItem(position);
                showScoreDialog(selectedSong);
            }
        });

        new GetSong() {
            @Override
            protected void onPostExecute(String result) {
                songList = JSONArrayToSongList.parseSongs(result);
                ArrayAdapter songAdapter = new ArrayAdapter<>(ScoreboardActivity.this, android.R.layout.simple_list_item_1, songList);
                songListView.setAdapter(songAdapter);
            }
        }.execute("*"); // retrieve all songs
    }

    private void showScoreDialog(Song selectedSong) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ScoreboardActivity.this);
        builder.setMessage("Song: " + selectedSong.getTitle() +
                "\nArtist: " + selectedSong.getArtist() +
                "\nScore: " + selectedSong.getUserScore() +
                "\nDifficulty: " + selectedSong.getDifficulty())
                .setTitle("Score Information");
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_scoreboard, menu);
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
}
