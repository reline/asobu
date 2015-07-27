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

    public long getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
