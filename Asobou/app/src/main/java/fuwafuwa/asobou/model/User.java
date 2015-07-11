package fuwafuwa.asobou.model;

/**
 * Created by mena on 7/10/2015.
 *
 * User
 * holds user info
 */
public class User {
    private int userid;
    private String phonebnumber;
    private String username;


    public int getUser_id() {
        return userid;
    }

    public void setUser_id(int userid) {
        this.userid = userid;
    }

    public String getPhonebnumber() {
        return phonebnumber;
    }

    public void setPhonebnumber(String phonebnumber) {
        this.phonebnumber = phonebnumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
