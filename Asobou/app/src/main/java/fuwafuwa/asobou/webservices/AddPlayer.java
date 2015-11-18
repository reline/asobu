package fuwafuwa.asobou.webservices;

import android.os.AsyncTask;

public class AddPlayer extends AsyncTask<String, Void, Void> {

    private static final String ADD_PLAYER = HttpManager.SERVER + "/updateplayer.php";

    // ID
    // UserName
    // PhoneNumber
    // DigitsID

    @Override
    protected Void doInBackground(String... player) {
        HttpManager.postData(ADD_PLAYER, "UserName=" + player[0] + "&" + "PhoneNumber=" + player[1]
            + "&" + "DigitsID=" + player[2]);
        return null;
    }
}
