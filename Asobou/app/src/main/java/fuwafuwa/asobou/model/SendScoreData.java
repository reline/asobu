package fuwafuwa.asobou.model;

import android.os.AsyncTask;
import fuwafuwa.asobou.HttpManager;

public class SendScoreData extends AsyncTask<String, String, String> {

    private String data = "";

    public SendScoreData(String data) {
        this.data = data;
    }

    @Override
    protected String doInBackground(String... params) {
        HttpManager.postData(params[0], data);
        return params[0];
    }
}
