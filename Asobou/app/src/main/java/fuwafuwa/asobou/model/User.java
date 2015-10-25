package fuwafuwa.asobou.model;

import android.os.Parcel;
import android.os.Parcelable;

public class User implements Parcelable {

    private String id;
    private String digitsSessionId; // TODO: implement after adding to user table in database
    private String userName;
    private String phoneNumber;

    public static User currentUser;

    // create a default/guest user account
    public User() {
        this.id = "0";
        this.userName = "Guest";
        this.phoneNumber = "1234567890";
    }

    public User(String id, String userName, String phoneNumber) {
        this.id = id;
        this.userName = userName;
        this.phoneNumber = phoneNumber;
    }

    public User(Parcel parcel) {
        id = parcel.readString();
        digitsSessionId = parcel.readString();
        userName = parcel.readString();
        phoneNumber = parcel.readString();
    }

    @Override
    public int describeContents() {
        return hashCode();
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(digitsSessionId);
        parcel.writeString(userName);
        parcel.writeString(phoneNumber);
    }

    public static final Parcelable.Creator<User> CREATOR = new Parcelable.Creator<User>() {
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    // GETTERS

    public String getId() {
        return id;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getUserName() {
        return this.userName;
    }

    public static User getCurrentUser() {
        return currentUser;
    }


    // SETTERS

    public void setId(String id) {
        this.id = id;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public static void setCurrentUser(User user) {
        currentUser = user;
    }
}
