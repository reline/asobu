package fuwafuwa.asobou.model;

import android.os.AsyncTask;

import java.net.URL;
import java.util.ArrayList;

import fuwafuwa.asobou.HttpManager;

public class RetrieveLyricsFile extends AsyncTask<String, String, String>
{
    private ArrayList<LyricLine> lyrics = new ArrayList<>();
    private URL lyricFile;
    private String lyricsRaw;
    private int lastTime = 0;

    @Override
    protected String doInBackground(String... params)
    {
        return HttpManager.getData(params[0]);
    }

    @Override
    protected void onPostExecute(String result) {
        lyricsRaw = result;
        parseLyrics(result);
    }

    private void parseLyrics(String lyricsRaw) {
        String[] rawArray = lyricsRaw.split(";"); // line separator ????
        int lineNumber = 1;
        for (String line:rawArray) {
            String startLyrics = line.substring(0,8);
            String endLyrics = line.substring(9,17);
            String lyricLine = line.substring(18, line.length());

            if(rawArray[0].equals(line)) { // invis chars ftw
                startLyrics = line.substring(1,9);
                endLyrics = line.substring(10,18);
                lyricLine = line.substring(19, line.length());
            }

            lyrics.add(new LyricLine(lineNumber++, startLyrics, endLyrics, lyricLine));
        }
    }

    public String getLyrics(int currTime) {
        // get four lines worth of lyrics or the rest if less than four remaining
        String newLine = "";

        for (LyricLine line : lyrics) {
            if(line.startOfLine(currTime)) {
                int ind = lyrics.indexOf(line);
                int inc = ind + 1;
                newLine = line.getLine();
                while(inc > lyrics.size() && (inc < ind + 3)) {
                    newLine += (lyrics.get(inc).getLine());
                    inc++;
                }
            }
        }
        lastTime = currTime;
        return newLine;
    }

    public boolean newLine(int currTime) {
        for (LyricLine line : lyrics) {
            if(line.startOfLine(currTime)) {
                return (lastTime != currTime);
            }
        }
        return false;
    }

}