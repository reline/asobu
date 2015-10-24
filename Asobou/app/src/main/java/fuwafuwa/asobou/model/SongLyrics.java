package fuwafuwa.asobou.model;

import java.util.ArrayList;
import java.util.List;

public class SongLyrics {

    private static List<LyricLine> lyricLines;
    private static int nextLineNumber = 0;

    SongLyrics() {
        lyricLines = new ArrayList<>();
    }

    void addLine(LyricLine lyricLine) {
        lyricLines.add(lyricLine);
    }

    LyricLine retrieveNextLine() {
        return lyricLines.get(nextLineNumber);
    }

    String getNextLine() { // DEBUG
        return lyricLines.get(nextLineNumber).toString();
    }

    int getCurrentLineNumber() { // DEBUG
        return nextLineNumber - 1; // returns -1 if a line number has not been retrieved yet
    }

    int getNextLineNumber() { // DEBUG
        return nextLineNumber;
    }
}
