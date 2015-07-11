package fuwafuwa.asobou.parser;

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

    public static List<Song> parseSongs(String...content) {
        try{
            /*
            * changed the gradle min version from 15 to 19 because JSONArray is only supported from 19 up
              * TODO: change code to account for other versions of android to support JSON from 15+
            */
            JSONArray array = new JSONArray(content);

            List<Song> songList = new ArrayList<>();

            for (int i=0; i < array.length(); i++){
                JSONObject obj = array.getJSONObject(i);
                Song song = new Song();

                //song
                song.setSongId(obj.getInt("song_id"));
                song.setTitle(obj.getString("name"));
                song.setArtist(obj.getString("artist"));
                song.setAlbum(obj.getString("album"));
                song.setGenre(obj.getString("genre"));
                song.setLength(obj.getString("length"));
                song.setDifficulty(obj.getString("diff"));
                song.setAlbumArtwork(obj.getString("artwork"));

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
    }   //end pasrseSongs

}   //end SongJSON parser class
