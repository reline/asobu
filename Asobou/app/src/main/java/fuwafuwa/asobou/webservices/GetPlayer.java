package fuwafuwa.asobou.webservices;

import android.os.AsyncTask;

public class GetPlayer extends AsyncTask<String, String, String> {

    private static final String GET_PLAYER = HttpManager.SERVER + "/getplayer.php";

    // ID
    // UserName
    // PhoneNumber
    // DigitsID

    @Override
    protected String doInBackground(String... digitsID) {
        return HttpManager.getData(GET_PLAYER, "DigitsID=" + digitsID[0]);
    }
}
