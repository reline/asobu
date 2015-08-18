package fuwafuwa.asobou;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import fuwafuwa.asobou.model.SendScoreData;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        String userId = "add_user_id=1";
        String songId = "add_song_id=1";
        String score = "add_score=100";
        String date = "add_date=2015-08-18 12:00:00";
        String and = "&"; // pretty pointless, I know

        SendScoreData testScore = new SendScoreData(userId + and + songId + and + score + and + date);// http://198.199.94.36/change/backend/addscore.php
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
