package fuwafuwa.asobou;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import fuwafuwa.asobou.model.PostData;
import fuwafuwa.asobou.model.User;

public class AboutActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date today = Calendar.getInstance().getTime();
        String date = df.format(today);

        // THIS DOESN'T WORK
        /*String userId = "add_user_id=" + User.currentUser.getId();
        String songId = "add_song_id=" + song.getId();
        String temp = "add_score=" + (String.valueOf(score));
        char[] addscore = new char[temp.length()];
        for (int i = 0; i < temp.length(); i++) {
            addscore[i] = temp.charAt(i);
        }
        String adddate = "add_date=" + date; //date2015-08-18 12:00:00";
        String and = "&"; // pretty pointless, I know
        PostData testScore = new PostData(userId + and + songId + and + addscore + and + adddate);// http://198.199.94.36/change/backend/addscore.php
        testScore.execute("http://198.199.94.36/change/backend/addscore.php");*/

        // TODO: get dynamic score POST to work
        // TODO: check if 'edit' or 'add' POST is needed
        // THIS WORKS
        String id = "8";
        int number = 8;
        int score = 1000;
        String scr = Integer.toString(score);
        String userId = "add_user_id=" + id;
        String songId = "add_song_id=" + number;
        String newScore = "add_score=" + scr; // score won't push if passed an int
        String adddate = "add_date=" + date; //date2015-08-18 12:00:00";
        PostData testScore = new PostData(userId + "&" + songId + "&" + newScore + "&" + adddate);// http://198.199.94.36/change/backend/addscore.php
        testScore.execute("http://198.199.94.36/change/backend/addscore.php");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_about, menu);
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
