package sg.edu.np.mad.team5MADAssignmentOnTask;

public class User {
    private String username, password,displayname, ImageURI;

    public User() {
    }

    public User(String username, String password, String displayname, String ImageURI) {
        this.username = username;
        this.password = password;
        this.displayname = displayname;
        this.ImageURI = ImageURI;
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

    public String getImageURI(){return ImageURI;};
    public void setImageURI(String ImageURI){this.ImageURI = ImageURI;}
}