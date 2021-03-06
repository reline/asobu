package fuwafuwa.asobou.parser;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import fuwafuwa.asobou.model.Song;

//TODO: change the name of this to include all objects...want to parse user data also

public class SongJSONparser {
    private static final String TAG = "JSONParser";
    public static List<Song> parseSongs(String content) {
        try{
            /*
            * changed the gradle min version from 15 to 19 because JSONArray is only supported from 19 up
            * if needed, JSONArray will be rid away with. API version is more important.
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
                song.setId(obj.getInt("song_id"));
                song.setTitle(obj.getString("song_name"));
                try {
                    song.setArtist(obj.getString("song_artist"));
                    song.setAlbum(obj.getString("song_album"));
                    song.setGenre(obj.getString("song_genre"));

                    String[] split = obj.getString("song_length").split(":");
                    int hours = Integer.parseInt(split[0]);
                    int minutes = Integer.parseInt(split[1]) + hours*60;
                    int seconds = Integer.parseInt(split[2]) + minutes*60;
                    song.setLength(seconds);

                    song.setDifficulty(obj.getString("song_diff")); // "slow", "medium", "fast"
                    song.setAlbumArtwork(obj.getString("album_artwork"));

                    //media
                    //song.setYoutubeLink("WIKqgE4BwAY");
                    song.setYoutubeLink(obj.getString("link"));

                    //lyrics
                    song.setLyricsKanji(obj.getString("link_to_file"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // this will probably break if i don't leave it in a different try/catch
                try {
                    //userscore
                    song.setUserScore(obj.getInt("score"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
