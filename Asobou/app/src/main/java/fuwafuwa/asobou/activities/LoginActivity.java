package fuwafuwa.asobou.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import fuwafuwa.asobou.R;
import fuwafuwa.asobou.model.Keys;
import fuwafuwa.asobou.model.Player;
import fuwafuwa.asobou.webservices.AddPlayer;
import fuwafuwa.asobou.webservices.GetPlayer;
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private static final String TAG = "LOGINACTIVITY";

    private String digitsID;
    private String PHONE_NUMBER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(Keys.TWITTER_KEY, Keys.TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());

        setContentView(R.layout.activity_login);

        PHONE_NUMBER = getPhoneNumber();

        Button loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Digits.authenticate(new AuthCallback() {
                    @Override
                    public void success(final DigitsSession digitsSession, String s) {
                        digitsID = Long.toString(digitsSession.getId());
                        login();
                    }

                    @Override
                    public void failure(DigitsException e) {
                        System.out.println("Digits authentication failure.");
                    }
                });
            }
        });
    }

    public void login() {
        new GetPlayer() {
            @Override
            protected void onPostExecute(String result) {

                Log.d(TAG, "onPostExecute");

                if(result != null) {
                    Log.d(TAG, "result != null");
                    try {
                        JSONObject object = new JSONObject(result);
                        Player.currentPlayer = new Player(Integer.parseInt(object.getString("ID")), object.getString("UserName"),
                                object.getString("PhoneNumber"), object.getString("DigitsID"));
                        Log.d(TAG, "currentPlayer.toString() = " + Player.currentPlayer.toString());
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    loginSuccessful();
                } else {
                    Log.d(TAG, "result == null");
                }

                if(Player.currentPlayer == null) {
                    Log.d(TAG, "currentPlayer == null");
                    Player.currentPlayer = new Player(0, "Username", PHONE_NUMBER, digitsID);
                    Log.d(TAG, "currentPlayer.toString() = " + Player.currentPlayer.toString());
                    String[] playerParams = {"Username", Player.currentPlayer.getPhoneNumber(),
                            Player.currentPlayer.getDigitsID()};
                    new AddPlayer() {
                        @Override
                        protected void onPostExecute(Void result) {
                            loginSuccessful();
                        }
                    }.execute(playerParams);
                }

            }
        }.execute(digitsID);
    }

    public void loginSuccessful() {
        // Show the signed-in alert
        showDialog(R.string.signed_in, R.string.signin_status_changed);

        new Handler().postDelayed(new Runnable() {
            public void run() {
                // go to maps activity
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                LoginActivity.this.startActivity(intent);
                finish();
            }
        }, 2000);

    }

    private void showDialog(int message, int title) {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setMessage(message)
                .setTitle(title);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private String getPhoneNumber() {
        TelephonyManager telephonyManager = (TelephonyManager) getApplicationContext().
                getSystemService(Context.TELEPHONY_SERVICE);
        return telephonyManager.getLine1Number();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
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
