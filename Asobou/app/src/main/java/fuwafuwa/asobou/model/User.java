package fuwafuwa.asobou.model;

public class User {

    private String id;
    private String digitsSessionId; // TODO: implement after adding to user table in database
    private String userName = "";
    private String phoneNumber = "";

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
}
