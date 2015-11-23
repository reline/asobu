package fuwafuwa.asobou.webservices;

import android.os.AsyncTask;

public class GetSong extends AsyncTask<String, String, String> {

    private static final String GET_SONG = HttpManager.SERVER + "/getsong.php";

    // ID *required*; if *, get all
    // Name
    // Artist
    // Album
    // Genre
    // Length
    // Difficult
    // LyricsID

    @Override
    protected String doInBackground(String... id) {
        return HttpManager.getData(GET_SONG, "ID=" + id[0]);
    }
}