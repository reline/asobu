package fuwafuwa.asobou.model;

public class LyricLine {
    private int lineNumber;
    private int startTime;
    private int endTime;
    private String lyrics;

    public LyricLine(int lineNumber, String startTime, String endTime, String lyrics) {
        this.lineNumber = lineNumber;
        this.startTime = convertToSeconds(startTime);
        //this.endTime = convertToSeconds(endTime);
        this.lyrics = lyrics;
    }

    public boolean startLyrics(int currTime) {
        return (startTime == currTime);
    }

    public boolean endLyrics(int currTime) {
        return (endTime == currTime);
    }

    public String getLine() {
        return (lyrics + "\n");
    }

    private int convertToSeconds(String arg) {
        String[] hms = arg.split(":");
        for(int i = 0; i < hms.length; i++) {
            if(hms[i].substring(0,1).equals("0")) {
                hms[i] = hms[i].substring(1,2);
            }
        }
        return ((Integer.parseInt(hms[0]) * 3600) + (Integer.parseInt(hms[1]) * 60) + Integer.parseInt(hms[2]));
    }
}
