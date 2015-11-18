package fuwafuwa.asobou.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fuwafuwa.asobou.R;
import fuwafuwa.asobou.model.Player;
import fuwafuwa.asobou.webservices.HttpManager;
import fuwafuwa.asobou.model.Song;
import fuwafuwa.asobou.model.SongJSONparser;

public class ScoreboardActivity extends AppCompatActivity {

    private static final String TAG = "ScoreboardActivity";

    private static final String GET_SCORES = "http://198.199.94.36/change/backend/getscoreinfo.php"; // requires a user id

    private ArrayList<Song> songList = new ArrayList<>();
    private ArrayList<Song> filteredSongList = new ArrayList<>();
    private ListView songListView;
    private boolean songAscending = true;
    private boolean artistAscending = true;
    private String currSort = "Song";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        //get the spinner to display the difficulty levels
        Spinner diffView = (Spinner) findViewById(R.id.scoreboard_diffspinner);
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.order_diff_spinner, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diffView.setAdapter(spinnerAdapter);
        diffView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getAdapter().getItem(position);
                if(filteredSongList != null) {
                    if (currSort.equals("Song")) {
                        filterSongList(currSort, songAscending, selectedItem);
                    } else {
                        filterSongList(currSort, artistAscending, selectedItem);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        songListView = (ListView) findViewById(R.id.scoreboard_listview);
        //output = (TextView) findViewById(R.id.textView2);
        if(isOnline()){
            requestData(GET_SCORES + "?" + "user_id=" + Player.currentPlayer.getId()); // TODO: make user accessible throughout activities
        } else {
            Toast.makeText(this, "The network is currently unavailable, check your connection.", Toast.LENGTH_SHORT).show();
        }

        songListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Song selectedSong = (Song) parent.getAdapter().getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(ScoreboardActivity.this);
                builder.setMessage("Song: " + selectedSong.getTitle() +
                                    "\nArtist: " + selectedSong.getArtist() +
                                    "\nScore: " + selectedSong.getUserScore() +
                                    "\nDifficulty: " + selectedSong.getDifficulty()).setTitle("Score Information");
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });

        // sort by song title
        Button sortSongButton = (Button) findViewById(R.id.sort_by_song);
        sortSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songAscending = !songAscending;
                sortSongList(songAscending);
                currSort = "Song";
                artistAscending = false;
            }
        });
        // sort by artist
        Button sortArtistButton = (Button) findViewById(R.id.sort_by_artist);
        sortArtistButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                artistAscending = !artistAscending;
                sortSongList(artistAscending);
                currSort = "Artist";
                songAscending = false;
            }
        });
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

    public void onSettingsButtonClick(MenuItem menuItem) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void onHelpButtonClick(MenuItem menuItem) {
        startActivity(new Intent(this, HelpActivity.class));
    }

    protected void updateDisplay() {

        if(songList != null) {
            filterSongList("Song", true, "All"); // sort song list by ascending song title by default
            ArrayAdapter<Song> songAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredSongList);
            songListView.setAdapter(songAdapter);
        }
    }

    private void requestData(String uri){

        ScoreboardTasks task = new ScoreboardTasks();
        task.execute(uri);
    }

    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        return ((netInfo != null) &&(netInfo.isConnectedOrConnecting()));
    }

    private class ScoreboardTasks extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            return ""; //HttpManager.getData(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            songList = (ArrayList<Song>)SongJSONparser.parseSongs(result);
            filteredSongList = songList;
            updateDisplay();
        }

        @Override
        protected void onProgressUpdate(String... values) {

        }
    }   //end Scoreboard Tasks

    public void sortSongList(final boolean ascending) {
        Collections.sort(filteredSongList, new Comparator<Song>() {
            @Override
            public int compare(Song lhs, Song rhs) {
                // TODO: include artists in scoreinfo GET request
                if (ascending) {
                    return (lhs.getTitle().compareTo(rhs.getTitle()));
                } else {
                    return (-lhs.getTitle().compareTo(rhs.getTitle()));
                }
            }
        });
        ArrayAdapter<Song> songAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredSongList);
        songListView.setAdapter(songAdapter);
    }

    public void filterSongList(String sortBy, Boolean sortMethod, String filter) {
        filteredSongList = new ArrayList<>();
        switch (filter) {
            case "Slow":
                // show slow songs
                for (Song song : songList) {
                    if (song.getDifficulty().equals("slow")) {
                        filteredSongList.add(song);
                    }
                }
                break;
            case "Medium":
                // show medium songs
                for (Song song : songList) {
                    if (song.getDifficulty().equals("medium")) {
                        filteredSongList.add(song);
                    }
                }
                break;
            case "Fast":
                // show fast songs
                for (Song song : songList) {
                    if (song.getDifficulty().equals("fast")) {
                        filteredSongList.add(song);
                    }
                }
                break;
            default:
                filteredSongList = songList;
                break;
        }
        sortSongList(sortMethod);
        ArrayAdapter<Song> songAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredSongList);
        songListView.setAdapter(songAdapter);
    }

}   //end scoreboard activity
