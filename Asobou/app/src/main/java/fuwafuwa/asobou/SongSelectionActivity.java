package fuwafuwa.asobou;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
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
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.util.ArrayList;

import fuwafuwa.asobou.model.Song;
import fuwafuwa.asobou.parser.SongJSONparser;

public class SongSelectionActivity extends AppCompatActivity {//Activity implements AdapterView.OnItemClickListener{

    private static final String TAG = "SongSelectionActivity";
    private String weburl = "http://198.199.94.36/change/backend/getallsongs.php";
    private ListView songListView;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<Song> songList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_selection);

        //get the spinner to display the difficulty levels
        Spinner diffView = (Spinner) findViewById(R.id.selectsong_diff);
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.order_diff_spinner, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diffView.setAdapter(spinnerAdapter);

        if(isOnline()){
            requestData(weburl);
        } else {
            Toast.makeText(this, "Network isn;t available, you're offline", Toast.LENGTH_SHORT).show();
        }

        /*

        songList.add("ド・キ・ド・キ　モーニン");
        songList.add("メギツネ");
        songList.add("ギッミチョコ");
        songList.add("イ～ネ");
        songList.add("Love Metal");
        songList.add("Love Machine");
        songList.add("Song 4 (Black Night)");
        songList.add("いいね!");
        songList.add("Headbangeeeeeeeeeeeeeerrrrrrrrrrr!!!!!!");
        songList.add("Onedari Dalsakusen");
        songList.add("Tamashii no Rufuran");
        songList.add("Catch me if you can");
        songList.add("Uki Uki Nightmare");
        songList.add("Rondo of Nightmare");
        songList.add("イジメ, ダメ, ゼッタイ");
        songList.add("Road of Resistance");
        songList.add("Akatsuki");
        songList.add("BABYMETAL DEATH");
        songList.add("君とアニメが見たい");

        songListView = (ListView) findViewById(R.id.selectsong_listview);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songList);
        songListView.setAdapter(listAdapter);

        */

    }

    /*
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String clickedSongId = (String) parent.getItemAtPosition(position);
        startActivity(new Intent(this, PlayTapModeActivity.class).putExtra("songVideoLink", getVideoLink(clickedSongId)));
    }

    public String getVideoLink(String songName) {
        for (Song s : songList) {
            if (s.getVideoLink().equals(songName)) {
                return s.getVideoLink();
            }
        }
        return "dQw4w9WgXcQ"; // if that song has no link, get rick rolled
    }*/

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_song_selection, menu);
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

        ArrayList<String> songTitles = new ArrayList<>();

        if(songList != null) {
            for(Song song : songList) {
                Log.d(TAG, " - updateDisplay: " + song.getTitle());
                //output.append(song.getTitle() + "\n");
                songTitles.add(song.getTitle());
            }

            ArrayAdapter<String> songAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, songTitles);
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
