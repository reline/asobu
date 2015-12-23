package fuwafuwa.asobou.model;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

//TODO: change the name of this to include all objects...want to parse user data also

public class JSONArrayToSongList {

    private static final String TAG = "JSONParser";

    public static List<Song> parseSongs(String content) {
        try{
            /*
            * changed the gradle min version from 15 to 19 because JSONArray is only supported from 19 up
            * if needed, JSONArray will be rid away with. API version is more important.
              * TODO: change code to account for other versions of android to support JSON from 15+
            */
            JSONArray array = new JSONArray(content);

            // Log.d(TAG, "- Content: " + content);

            // Log.d(TAG, "- JSONarray: " + array.toString());

            List<Song> songList = new ArrayList<>();

            for (int i=0; i < array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                //Log.d(TAG, " JSONobj: " + obj.toString());

                String[] length = obj.getString("Length").split(":");
                int hours = Integer.parseInt(length[0]);
                int minutes = Integer.parseInt(length[1]) + hours*60;
                int seconds = Integer.parseInt(length[2]) + minutes*60;

                Song song = new Song(obj.getInt("ID"), obj.getString("Name"), obj.getString("Artist"),
                        obj.getString("Album"), obj.getString("Genre"), seconds, obj.getString("Difficult"));

                songList.add(song);
            }
            return songList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

}