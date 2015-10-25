package fuwafuwa.asobou;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
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

    public static String GET_ALL_USERS = "http://198.199.94.36/change/backend/getallnewusers.php";
    public static String ADD_USER = "http://198.199.94.36/change/backend/addnewuser.php";

    private static final String TAG = "LoginActivity";
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
                    public void success(final DigitsSession digitsSession, String s) {
                        digitsSessionId = Long.toString(digitsSession.getId());
                        login();
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

    public void login() {
        RetrieveUser retrieveUser = new RetrieveUser();
        retrieveUser.execute(digitsSessionId);
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

    // class created here instead of a new file so we don't have to pass the activity to RetrieveUser
    private class RetrieveUser extends AsyncTask<String, String, User> {

        private List<User> userList = new ArrayList<>();
        private User currentUser;

        @Override
        protected User doInBackground(String... strings) {
            String data =  HttpManager.getData(GET_ALL_USERS); // make an http request for the users in the db
            parseUsers(data); // parse the users into an array list
            setCurrentUser(digitsSessionId);
            return currentUser;
        }

        @Override
        protected void onPostExecute(User result) {

            // we move to the next activity with the new user in hand!
            startActivity(new Intent(LoginActivity.this, DashboardActivity.class)
                    .putExtra("DIGITS_SESSION_ID", result));
        }

        private void parseUsers(String data) {
            try {
                // put the result of the GET request into a JSONArray for parsing
                JSONArray array = new JSONArray(data);
                for (int i = 0; i < array.length(); i++) {
                    JSONObject obj = array.getJSONObject(i);
                    User user = new User(obj.getString("user_id"), obj.getString("username"), obj.getString("phonenumber"));
                    userList.add(user);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private void setCurrentUser(String user) {
            currentUser = findCurrentUser(user);
            if (currentUser == null) {
                addUserToDb(user);
                currentUser = findCurrentUser(user);
                // if currentUser is still null, set him as guest user profile
                if (currentUser == null) {
                    currentUser = new User();
                }
            }
        }

        private User findCurrentUser(String user) {
            // look for our user, if they are not found in the db then return null
            for (int i = 0; i < userList.size(); i++) {
                if(userList.get(i).getUserName().equals(user)) { // TODO: change db for id compatibility
                    return userList.get(i);
                }
            }
            return null;
        }

        private void addUserToDb(String user) {
            HttpManager.postData(ADD_USER,
                    "add_username=" + user + "&" + "add_phone=" + "none");
        }
    }

}
