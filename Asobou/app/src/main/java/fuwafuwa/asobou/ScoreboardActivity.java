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
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import fuwafuwa.asobou.model.Song;
import fuwafuwa.asobou.parser.SongJSONparser;

public class ScoreboardActivity extends AppCompatActivity {

    private static final String TAG = "ScoreboardActivity";

    private String weburl = "http://198.199.94.36/change/backend/getallsongs.php";
    //private TextView output;
    private List<Song> songList = new ArrayList<>();
    private ListView songListView;
    //private ArrayAdapter<String> songAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scoreboard);

        //get the spinner to display the difficulty levels
        Spinner diffView = (Spinner) findViewById(R.id.scoreboard_diffspinner);
        ArrayAdapter spinnerAdapter = ArrayAdapter.createFromResource(this, R.array.order_diff_spinner, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        diffView.setAdapter(spinnerAdapter);

        songListView = (ListView) findViewById(R.id.scoreboard_listview);

        //output = (TextView) findViewById(R.id.textView2);
        if(isOnline()){
            requestData(weburl);
        } else {
            Toast.makeText(this, "The network is currently unavailable, check your connection.", Toast.LENGTH_SHORT).show();
        }

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

        ScoreboardTasks task = new ScoreboardTasks();
        //task.execute("ド・キ・ド・キ　モーニン", "メギツネ", "ギッミチョコ", "イ～ネ", "君とアニメが見たい");
        task.execute(uri);
    }

    protected boolean isOnline(){
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if((netInfo != null) &&(netInfo.isConnectedOrConnecting())){
            return true;
        } else {
            return false;
        }
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
            songList = SongJSONparser.parseSongs(result);
            updateDisplay();
        }

        @Override
        protected void onProgressUpdate(String... values) {
            //updateDisplay(values[0]);
        }
    }   //end Scoreboard Tasks

}   //end scoreboard activity
