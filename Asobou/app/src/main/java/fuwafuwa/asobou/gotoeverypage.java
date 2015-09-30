package fuwafuwa.asobou;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class gotoeverypage extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gotoeverypage);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_gotoeverypage, menu);
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

    /*
    dashbaord
    about
    help
    settings
    scoreboard
    select song
    play tap
    play type
     */

    public void onDashboardButtonClick(View v) {
        startActivity(new Intent(this, DashboardActivity.class));
    }

    public void onAboutClick(View v) {
        startActivity(new Intent(this, AboutActivity.class));
    }

    public void onHelpClick(View v) {
        startActivity(new Intent(this, HelpActivity.class));
    }

    public void onSettingsButtonClick(View v) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void onScoreboardButtonClick(View v) {
        startActivity(new Intent(this, ScoreboardActivity.class));
    }

    public void onSongSelectionButtonClick(View v) {
        startActivity(new Intent(this, SongSelectionActivity.class));
    }

    public void onPlayTapButtonClick(View v) {
        startActivity(new Intent(this, PlayTapModeActivity.class));
    }

    public void onPlayTypeButtonClick(View v) {
        startActivity(new Intent(this, PlayTypeModeActivity.class));
    }
}
