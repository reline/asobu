package fuwafuwa.asobou.model;

/**
 * Created by mena on 7/10/2015.
 *
 * User
 * holds user info
 */
public class User {
    public static long id;

    private String phonenumber;
    private String username;


    public long getUser_id() {
        return id;
    }

    public void setUser_id(int id) {
        this.id = id;
    }

    public String getPhonebnumber() {
        return phonenumber;
    }

    public void setPhonebnumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
