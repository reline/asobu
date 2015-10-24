package fuwafuwa.asobou;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fuwafuwa.asobou.model.Song;
import fuwafuwa.asobou.parser.SongJSONparser;

public class SongSelectionActivity extends AppCompatActivity {//Activity implements AdapterView.OnItemClickListener{

    // TODO: make sort the same as scoreboard

    private static final String TAG = "SongSelectionActivity";

    private String weburl = "http://198.199.94.36/change/backend/getsongselection.php";

    private ArrayList<Song> songList = new ArrayList<>();
    private ArrayList<Song> filteredSongList = new ArrayList<>();
    private ListView songListView;
    private boolean songAscending = true;
    private boolean artistAscending = true;
    private String currSort = "Song";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_selection);

        //get the spinner to display the difficulty levels
        Spinner diffView = (Spinner) findViewById(R.id.selectsong_diff);
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.order_diff_spinner, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diffView.setAdapter(spinnerAdapter);

        diffView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedItem = (String) parent.getAdapter().getItem(position);
                if(currSort.equals("Song")) {
                    filterSongList(currSort, songAscending, selectedItem);
                } else {
                    filterSongList(currSort, artistAscending, selectedItem);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        // get songs and place them in the listview
        songListView = (ListView) findViewById(R.id.selectsong_listview);
        if(isOnline()){
            requestData(weburl);
        } else {
            Toast.makeText(this, "The network is currently unavailable, check your connection.", Toast.LENGTH_SHORT).show();
        }

        songListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Song selectedSong = (Song) parent.getAdapter().getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(SongSelectionActivity.this);
                builder.setMessage(R.string.song_mode_dialog_message).setTitle(R.string.song_mode_dialog_title);
                builder.setPositiveButton(R.string.tap_mode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(SongSelectionActivity.this, PlayTapModeActivity.class).putExtra("song", selectedSong));
                    }
                });
                builder.setNeutralButton(R.string.type_mode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(SongSelectionActivity.this, PlayTypeModeActivity.class).putExtra("song", selectedSong));
                    }
                });
                builder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // user canceled dialog
                    }
                });

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

    protected void updateDisplay() {
        if(songList != null) {
            filterSongList("Song", true, "All"); // sort song list by ascending song title by default
            ArrayAdapter<Song> songAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, filteredSongList);
            songListView.setAdapter(songAdapter);
        }
    }

    private void requestData(String uri){

        SelectSongTask task = new SelectSongTask();
        task.execute(uri);
    }

    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();

        return (netInfo != null && netInfo.isConnectedOrConnecting());
    }

    private class SelectSongTask extends AsyncTask<String, String, String>{

        @Override
        protected String doInBackground(String... params) {
            return HttpManager.getData(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            songList = (ArrayList<Song>) SongJSONparser.parseSongs(result);
            filteredSongList = songList;
            updateDisplay();
        }
    }   //end song select task

    public void sortSongList(final String sortBy, final boolean ascending) {
        try {
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
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
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

}   //end of activity
