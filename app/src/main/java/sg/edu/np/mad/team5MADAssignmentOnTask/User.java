package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String username, password,displayname, ImageURI, collaboratedtasks;
    private int eventCount, taskCount;
    private boolean isFriend;
    private List<String> friendList;
    public User() {
    }

    public User(String username, String password, String displayname, String ImageURI, int eventCount, int taskCount, String collaboratedtasks) {
        this.username = username;
        this.password = password;
        this.displayname = displayname;
        this.ImageURI = ImageURI;
        this.eventCount = eventCount;
        this.taskCount = taskCount;
        this.collaboratedtasks = collaboratedtasks;
        this.friendList = new ArrayList<>();
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

    public int getEventCount() {
        return eventCount;
    }

    public void setEventCount(int eventCount) {
        this.eventCount = eventCount;
    }

    public int getTaskCount() {
        return taskCount;
    }

    public void setTaskCount(int taskCount) {
        this.taskCount = taskCount;
    }

    public String getCollaboratedtasks() {
        return collaboratedtasks;
    }

    public void setCollaboratedtasks(String collaboratedtasks) {
        this.collaboratedtasks = collaboratedtasks;
    }
    public boolean isFriend() {
        return isFriend;
    }
    public void setFriend(boolean friend) {
        isFriend = friend;
    }
    public List<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(List<String> friendList) {
        this.friendList = friendList;
    }
}
