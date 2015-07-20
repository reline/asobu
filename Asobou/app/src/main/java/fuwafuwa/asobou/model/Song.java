package fuwafuwa.asobou.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

/**
 * Created by mena on 7/10/2015.
 *
 * Song Class
 * holds song info, media info, lyric info and also user score info
 */
public class Song implements Parcelable {
    //about the song
    private int songId;
    private String title;
    private String artist;
    private String album;
    private String genre;
    private String length;
    private String difficulty;
    private String albumArtwork;

    //media info
    private String youtubeLink;
    private String videoLink;
    private String songLink;

    //lyric info
    private String lyricsKana;
    private String lyricsKanji;
    private String lyricsRomaji;

    //user score info
    private int userScore;
    //TODO: implement datatime thing...

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(songId);
        dest.writeString(title);
        dest.writeString(artist);
        dest.writeString(album);
        dest.writeString(genre);
        dest.writeString(length);
        dest.writeString(difficulty);
        dest.writeString(albumArtwork);

        dest.writeString(youtubeLink);
        dest.writeString(videoLink);
        dest.writeString(songLink);

        dest.writeString(lyricsKana);
        dest.writeString(lyricsKanji);
        dest.writeString(lyricsRomaji);

        dest.writeInt(userScore);
    }

    public Song(Parcel parcel) {
        songId = parcel.readInt();
        title = parcel.readString();
        artist = parcel.readString();
        album = parcel.readString();
        genre = parcel.readString();
        length = parcel.readString();
        difficulty = parcel.readString();
        albumArtwork = parcel.readString();

        youtubeLink = parcel.readString();
        videoLink = parcel.readString();
        songLink = parcel.readString();

        lyricsKana = parcel.readString();
        lyricsKanji = parcel.readString();
        lyricsRomaji = parcel.readString();

        userScore = parcel.readInt();
    }

    public Song() {

    }

    public static final Parcelable.Creator<Song> CREATOR = new Parcelable.Creator<Song>() {
        @Override
        public Song createFromParcel(Parcel parcel) {
            return new Song(parcel);
        }

        @Override
        public Song[] newArray(int size) {
            return new Song[size];
        }
    };

    public String toString() {
        return getTitle();
    }

    public int getSongId() {
        return songId;
    }

    public void setSongId(int songId) {
        this.songId = songId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getAlbum() {
        return album;
    }

    public void setAlbum(String album) {
        this.album = album;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public String getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(String difficulty) {
        this.difficulty = difficulty;
    }

    public String getAlbumArtwork() {
        return albumArtwork;
    }

    public void setAlbumArtwork(String albumArtwork) {
        this.albumArtwork = albumArtwork;
    }

    public String getYoutubeLink() {
        return youtubeLink;
    }

    public void setYoutubeLink(String youtubeLink) {
        this.youtubeLink = youtubeLink;
    }

    public String getVideoLink() {
        return videoLink;
    }

    public void setVideoLink(String videoLink) {
        this.videoLink = videoLink;
    }

    public String getSongLink() {
        return songLink;
    }

    public void setSongLink(String songLink) {
        this.songLink = songLink;
    }

    public String getLyricsKana() {
        return lyricsKana;
    }

    public void setLyricsKana(String lyricsKana) {
        this.lyricsKana = lyricsKana;
    }

    public String getLyricsKanji() {
        return lyricsKanji;
    }

    public void setLyricsKanji(String lyricsKanji) {
        this.lyricsKanji = lyricsKanji;
    }

    public String getLyricsRomaji() {
        return lyricsRomaji;
    }

    public void setLyricsRomaji(String lyricsRomaji) {
        this.lyricsRomaji = lyricsRomaji;
    }

    public int getUserScore() {
        return userScore;
    }

    public void setUserScore(int userScore) {
        this.userScore = userScore;
    }

}
