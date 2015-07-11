package fuwafuwa.asobou;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.gson.internal.bind.ArrayTypeAdapter;

import java.util.ArrayList;

public class SongSelectionActivity extends AppCompatActivity {

    private ListView songListView;
    private ArrayAdapter<String> listAdapter;
    private ArrayList<String> songarray = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_song_selection);

        songarray.add("ド・キ・ド・キ　モーニン");
        songarray.add("メギツネ");
        songarray.add("ギッミチョコ");
        songarray.add("イ～ネ");
        songarray.add("Love Metal");
        songarray.add("Love Machine");
        songarray.add("Song 4 (Black Night)");
        songarray.add("いいね!");
        songarray.add("Headbangeeeeeeeeeeeeeerrrrrrrrrrr!!!!!!");
        songarray.add("Onedari Dalsakusen");
        songarray.add("Tamashii no Rufuran");
        songarray.add("Catch me if you can");
        songarray.add("Uki Uki Nightmare");
        songarray.add("Rondo of Nightmare");
        songarray.add("イジメ, ダメ, ゼッタイ");
        songarray.add("Road of Resistance");
        songarray.add("Akatsuki");
        songarray.add("BABYMETAL DEATH");
        songarray.add("君とアニメが見たい");

        songListView = (ListView) findViewById(R.id.selectsong_listview);
        listAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, songarray);
        songListView.setAdapter(listAdapter);

    }

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

    /*
    private class SelectSongTask extends AsyncTask<String, String, String>{

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
        }
    }
    */

}   //end of activity
