package sg.edu.np.mad.madassignment;

public class User {
    private String username, password,displayname;


    public User() {
    }

    public User(String username, String password,String displayname) {
        this.username = username;
        this.password = password;
        this.displayname = displayname;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDisplayname(){return  displayname;};
    public void setDisplayname(String displayname){this.displayname = displayname;}
}
