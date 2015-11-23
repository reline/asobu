package fuwafuwa.asobou.webservices;

import android.os.AsyncTask;

public class GetScore extends AsyncTask<String, String, String> {

    private static final String GET_SCORE = HttpManager.SERVER + "/getscore.php";

    // ID
    // SongID *required*; if *, get all song scores
    // PlayerDigitsID *required*
    // Score
    // Archived Date

    @Override
    protected String doInBackground(String... id) {
        return HttpManager.getData(GET_SCORE, "SongID=" + id[0] + "&" + "PlayerDigitsID=" + id[1]);
    }
}