package sg.edu.np.mad.madassignment;

public class Task {

    String username, title, type, date;
    Boolean Status;

    public Task() {
    }

    public Task(String username, String title, String type, String date,Boolean status) {
        this.username = username;
        this.title = title;
        this.type = type;
        this.date = date;
        this.Status = status;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        Status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
