package sg.edu.np.mad.team5MADAssignmentOnTask;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class User {
    private String username, password,displayname, ImageURI, collaboratedtasks, friendList;
    private int eventCount, taskCount;
    private boolean isFriend;
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
        this.friendList = "";
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
    public String getFriendList() {
        return friendList;
    }

    public void setFriendList(String friendList) {
        this.friendList = friendList;
    }

    // Add a new friend username to the friendList (comma-separated string format)
    public void addFriend(String friendUsername) {
        if (friendList.isEmpty()) {
            friendList = friendUsername;
        } else {
            friendList += "," + friendUsername;
        }
    }

    // Remove a friend username from the friendList (comma-separated string format)
    public void removeFriend(String friendUsername) {
        List<String> friendsList = new ArrayList<>(Arrays.asList(friendList.split(",")));
        friendsList.remove(friendUsername);
        friendList = TextUtils.join(",", friendsList);
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", displayname='" + displayname + '\'' +
                ", ImageURI='" + ImageURI + '\'' +
                ", collaboratedtasks='" + collaboratedtasks + '\'' +
                ", eventCount=" + eventCount +
                ", taskCount=" + taskCount +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return Objects.equals(getUsername(), user.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getUsername());
    }
}
