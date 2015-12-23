package fuwafuwa.asobou.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import fuwafuwa.asobou.R;
import fuwafuwa.asobou.model.Player;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setTitle("こんにちは " + Player.currentPlayer.getUserName() + "さん!");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void onScoreboardButtonClick(View v) {
        startActivity(new Intent(MainActivity.this, ScoreboardActivity.class));
    }

    public void onPlayButtonClick(View v) {
        startActivity(new Intent(this, SongSelectionActivity.class));
    }

    public void onSettingsButtonClick(MenuItem menuItem) {
        startActivity(new Intent(this, SettingsActivity.class));
    }

    public void onHelpButtonClick(MenuItem menuItem) {
        startActivity(new Intent(this, HelpActivity.class));
    }

    public void onAboutButtonClick(MenuItem menuItem) {
        startActivity(new Intent(this, AboutActivity.class));
    }
}
