package fuwafuwa.asobou;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;

import fuwafuwa.asobou.model.Song;
import fuwafuwa.asobou.parser.SongJSONparser;

public class SongSelectionActivity extends AppCompatActivity {//Activity implements AdapterView.OnItemClickListener{

    private static final String TAG = "SongSelectionActivity";

    private String weburl = "http://198.199.94.36/change/backend/getsongselection.php";

    private ArrayList<Song> songList = new ArrayList<>();
    private ListView songListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_selection);

        //get the spinner to display the difficulty levels
        Spinner diffView = (Spinner) findViewById(R.id.selectsong_diff);
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.order_diff_spinner, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diffView.setAdapter(spinnerAdapter);

        songListView = (ListView) findViewById(R.id.selectsong_listview);

        if(isOnline()){
            requestData(weburl);
        } else {
            Toast.makeText(this, "The network is currently unavailable, check your connection.", Toast.LENGTH_SHORT).show();
        }

        songListView.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Song selectedSong = (Song) parent.getAdapter().getItem(position);
                //String youtubeLink = "";

                /*for(Song song : songList) {
                    if(song.getTitle().equals(selectedSong)) {
                        selectedSong = song;
                        youtubeLink = song.getYoutubeLink();
                    }
                }*/

                Intent intent = new Intent(SongSelectionActivity.this, PlayTapModeActivity.class);
                //intent.putExtra("song_url", youtubeLink);
                intent.putExtra("song", selectedSong);
                startActivity(intent);
            }
        });

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

            ArrayAdapter<Song> songAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songList);
            songListView.setAdapter(songAdapter);
        }
    }

    private void requestData(String uri){

        SelectSongTask task = new SelectSongTask();
        //task.execute("ド・キ・ド・キ　モーニン", "メギツネ", "ギッミチョコ", "イ～ネ", "君とアニメが見たい");
        task.execute(uri);
        //task.onPostExecute();
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
            updateDisplay();
        }
    }   //end song select task


}   //end of activity
