package sg.edu.np.mad.madassignment;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username, password;
    private List todoList;

    public User() {
    }

    public User(String username, String password, List<TodoModel> todoList) {
        this.username = username;
        this.password = password;
        this.todoList = todoList;
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
}
