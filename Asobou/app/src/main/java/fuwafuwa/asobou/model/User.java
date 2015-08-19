package fuwafuwa.asobou.model;

/**
 * Created by mena on 7/10/2015.
 *
 * User
 * holds user info
 */
public class User {
    private String id;
    private String username = "";
    private String phonenumber = "";

    public static User currentUser;

    public User(String id, String username, String phonenumber) {
        this.id = id;
        this.username = username;
        this.phonenumber = phonenumber;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
