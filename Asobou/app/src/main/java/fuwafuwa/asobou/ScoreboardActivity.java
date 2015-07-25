package fuwafuwa.asobou;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import fuwafuwa.asobou.model.Song;
import fuwafuwa.asobou.parser.SongJSONparser;

public class ScoreboardActivity extends AppCompatActivity {

    private static final String TAG = "ScoreboardActivity";

    private String weburl = "http://198.199.94.36/change/backend/getsongselection.php"; //getallsongs

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
                if (currSort.equals("Song")) {
                    filterSongList(currSort, songAscending, selectedItem);
                } else {
                    filterSongList(currSort, artistAscending, selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        songListView = (ListView) findViewById(R.id.scoreboard_listview);
        //output = (TextView) findViewById(R.id.textView2);
        if(isOnline()){
            requestData(weburl);
        } else {
            Toast.makeText(this, "The network is currently unavailable, check your connection.", Toast.LENGTH_SHORT).show();
        }

        // sort by song title
        Button sortSongButton = (Button) findViewById(R.id.sort_by_song);
        sortSongButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                songAscending = !songAscending;
                sortSongList("Song",songAscending);
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
                sortSongList("Artist",artistAscending);
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
        //output.append(message + "\n");
        //ArrayList<String> songTitles = new ArrayList<>();

        if(songList != null) {
            /*for(Song song : songList) {
                Log.d(TAG, " - updateDisplay: " + song.getTitle());
                //output.append(song.getTitle() + "\n");
                songTitles.add(song.getTitle());
            }*/

            filterSongList("Song", true, "All"); // sort song list by ascending song title by default
            ArrayAdapter<Song> songAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredSongList);
            songListView.setAdapter(songAdapter);
        }
    }

    private void requestData(String uri){

        ScoreboardTasks task = new ScoreboardTasks();
        //task.execute("ド・キ・ド・キ　モーニン", "メギツネ", "ギッミチョコ", "イ～ネ", "君とアニメが見たい");
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
            //updateDisplay("Starting task...");
        }

        @Override
        protected String doInBackground(String... params) {
            /*
            for(int i=0; i<params.length; i++){
                publishProgress(params[i]);
            }
            return "Task Complete";
            */

            //String content = HttpManager.getData(params[0]);
            return HttpManager.getData(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            songList = (ArrayList<Song>)SongJSONparser.parseSongs(result);
            filteredSongList = songList;
            updateDisplay();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //updateDisplay(values[0]);
        }
    }   //end Scoreboard Tasks

    public void sortSongList(final String sortBy, final boolean ascending) {
        Collections.sort(filteredSongList, new Comparator<Song>() {
            @Override
            public int compare(Song lhs, Song rhs) {
                if (ascending) {
                    if (sortBy.equals("Artist")) {
                        return (lhs.getArtist().compareTo(rhs.getArtist()));
                    } else {
                        return (lhs.getTitle().compareTo(rhs.getTitle()));
                    }
                } else { // descending
                    if (sortBy.equals("Artist")) {
                        return (-lhs.getArtist().compareTo(rhs.getArtist()));
                    } else {
                        return (-lhs.getTitle().compareTo(rhs.getTitle()));
                    }
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
        sortSongList(sortBy, sortMethod);
        ArrayAdapter<Song> songAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredSongList);
        songListView.setAdapter(songAdapter);
    }

}   //end scoreboard activity
