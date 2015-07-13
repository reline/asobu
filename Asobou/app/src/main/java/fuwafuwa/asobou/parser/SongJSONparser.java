package fuwafuwa.asobou.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import fuwafuwa.asobou.model.Song;

/**
 * Created by mena on 7/10/2015.
 * parses Song JSON data
 */

//TODO: change the name of this to include all objects...want to pasrse user data also

public class SongJSONparser {
    private static final String TAG = "JSONParser";
    public static List<Song> parseSongs(String content) {
        try{
            /*
            * changed the gradle min version from 15 to 19 because JSONArray is only supported from 19 up
              * TODO: change code to account for other versions of android to support JSON from 15+
            */
            JSONArray array = new JSONArray(content);

            Log.d(TAG, "- Content: " + content);

            Log.d(TAG, "- JSONarray: " + array.toString());

            List<Song> songList = new ArrayList<>();

            for (int i=0; i < array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                Log.d(TAG, " JSONobj: " + obj.toString());
                Song song = new Song();

                //TODO: change php so that the col names exclude "song"
                //song
                song.setSongId(obj.getInt("song_id"));
                song.setTitle(obj.getString("song_name"));
                song.setArtist(obj.getString("song_artist"));
                song.setAlbum(obj.getString("song_album"));
                song.setGenre(obj.getString("song_genre"));
                song.setLength(obj.getString("song_length"));
                song.setDifficulty(obj.getString("song_diff"));
                song.setAlbumArtwork(obj.getString("album_artwork"));

                //media

                //lyrics


                //userscore

                //add song
                songList.add(song);
            }   //end for loop

            return songList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }   //end parseSongs

}   //end SongJSON parser class
