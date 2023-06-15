package sg.edu.np.mad.madassignment;

import java.util.ArrayList;
import java.util.List;

public class User {
    private static String username;
    private String password;

    private ArrayList<TodoModel> todolist;


    public User() {
    }

    public User(String username, String password,ArrayList todolist) {
        this.username = username;
        this.password = password;
        this.todolist = todolist;


    }

    public static String getUsername() {
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
}
