package fuwafuwa.asobou.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import fuwafuwa.asobou.R;
import fuwafuwa.asobou.model.JSONArrayToSongList;
import fuwafuwa.asobou.webservices.GetSong;
import fuwafuwa.asobou.model.Song;

public class SongSelectionActivity extends AppCompatActivity {//Activity implements AdapterView.OnItemClickListener{

    private static final String TAG = "SongSelectionActivity";

    private List<Song> songList = new ArrayList<>();
    private ListView songListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_selection);

        // get songs and place them in the listview
        songListView = (ListView) findViewById(R.id.selectsong_listview);
        new GetSong() {
            @Override
            protected void onPostExecute(String result) {
                songList = JSONArrayToSongList.parseSongs(result);
                ArrayAdapter songAdapter = new ArrayAdapter<>(SongSelectionActivity.this, android.R.layout.simple_list_item_1, songList);
                songListView.setAdapter(songAdapter);
            }
        }.execute("*"); // retrieve all songs


        songListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Song selectedSong = (Song) parent.getAdapter().getItem(position);

                AlertDialog.Builder builder = new AlertDialog.Builder(SongSelectionActivity.this);
                builder.setMessage(R.string.song_mode_dialog_message).setTitle(R.string.song_mode_dialog_title);
                builder.setPositiveButton(R.string.tap_mode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // startActivity(new Intent(SongSelectionActivity.this, PlayTapModeActivity.class).putExtra("song", selectedSong));
                    }
                });
                builder.setNeutralButton(R.string.type_mode, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // startActivity(new Intent(SongSelectionActivity.this, PlayTypeModeActivity.class).putExtra("song", selectedSong));
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

    }

}   //end of activity
