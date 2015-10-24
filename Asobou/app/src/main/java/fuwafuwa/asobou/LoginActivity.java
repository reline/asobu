package fuwafuwa.asobou;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import fuwafuwa.asobou.model.User;
import io.fabric.sdk.android.Fabric;
import com.twitter.sdk.android.core.TwitterCore;
import com.twitter.sdk.android.core.TwitterAuthConfig;
import com.digits.sdk.android.Digits;
import com.digits.sdk.android.AuthCallback;
import com.digits.sdk.android.DigitsException;
import com.digits.sdk.android.DigitsSession;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    // Store in database for retrieval when web services are available
    private static final String TWITTER_KEY = "QoRJuK2MljCoc6VG19INXBHwJ";
    private static final String TWITTER_SECRET = "FfnPO03pLwiXBmfQ9zivAZ1K6qSL8PFv10KVU66HIyetrDSrTe";

    private static final String TAG = "LoginActivity";
    private List<User> userList = new ArrayList<>();
    private String digitsSessionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
        Fabric.with(this, new TwitterCore(authConfig), new Digits());

        setContentView(R.layout.activity_login);

        Button loginButton = (Button) findViewById(R.id.login);
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Digits.authenticate(new AuthCallback() {
                    @Override
                    public void success(DigitsSession digitsSession, String s) {
                        digitsSessionId = Long.toString(digitsSession.getId());
                        RetrieveUsers getUsers = new RetrieveUsers();
                        getUsers.execute("http://198.199.94.36/change/backend/getallnewusers.php");
                    }

                    @Override
                    public void failure(DigitsException e) {
                        System.out.println("Digits authentication failure.");
                    }
                });
            }
        });

        Button aboutButton = (Button) findViewById(R.id.about_button);
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, AboutActivity.class));
            }
        });
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

    private class RetrieveUsers extends AsyncTask<String, String, String> {

        @Override
        protected String doInBackground(String... params) {
            return HttpManager.getData(params[0]);
        }

        @Override
        protected void onPostExecute(String result) {
            try {
                JSONArray array = new JSONArray(result);
                userList = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    User user = new User(obj.getString("user_id"), obj.getString("username"), obj.getString("phonenumber"));
                    userList.add(user);
                }
                Log.d(TAG, String.valueOf(userList.size()));
            } catch (JSONException e) {
                Log.d(TAG, "JSONException");
                e.printStackTrace();
            }
            User.currentUser = getUser(digitsSessionId);

            AssignUser(digitsSessionId);

            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
        }
    }   //end song select task

    private void AssignUser(String username) {
        // TODO: create digitsSessionId number in db

        // if that user_id is not found in the db
        if(User.currentUser == null) {
            HttpManager.postData("http://198.199.94.36/change/backend/addnewuser.php",
                    "add_username=" + username + "&" + "add_phone=" + "none");

            RetrieveUsers getUsers = new RetrieveUsers();
            getUsers.execute("http://198.199.94.36/change/backend/getallnewusers.php");
            User.currentUser = getUser(username);

            if(User.currentUser == null) { // if things are really messed up
                User.currentUser = new User("8", "bura", "none");
            }
        }
    }

    public User getUser(String username) {
        //Log.d(TAG, "Search: " + username);
        for (int i = 0; i < userList.size(); i++) {
            Log.d(TAG, String.valueOf(userList.get(i).getUserName()));
            if(userList.get(i).getUserName().equals(username)) { // TODO: change db for id compatibility
                return userList.get(i);
            }
        }
        return null;
    }
}
