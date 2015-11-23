package fuwafuwa.asobou.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Player implements Parcelable {

    public static Player currentPlayer;

    private int id;
    private String digitsID; // TODO: implement after adding to user table in database
    private String userName;
    private String phoneNumber;

    // create a default/guest user account
    public Player() {
        this.id = 0;
        this.userName = "Guest";
        this.phoneNumber = "1234567890";
    }

    public Player(int id, String userName, String phoneNumber, String digitsID) {
        this.id = id;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
        this.digitsID = digitsID;
    }

    public Player(Parcel parcel) {
        id = parcel.readInt();
        digitsID = parcel.readString();
        userName = parcel.readString();
        phoneNumber = parcel.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        parcel.writeString(digitsID);
        parcel.writeString(userName);
        parcel.writeString(phoneNumber);
    }

    public static final Parcelable.Creator<Player> CREATOR = new Parcelable.Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel parcel) {
            return new Player(parcel);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    // GETTERS

    public int getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserName() {
        return this.userName;
    }

    public String getDigitsID() {
        return this.digitsID;
    }

    @Override
    public String toString() {
        return this.getDigitsID();
    }

    // SETTERS

    public void setId(int id) {
        this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
