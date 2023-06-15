package sg.edu.np.mad.madassignment;

public class Event {

    String username, title, type, date;

    public Event() {
    }

    public Event(String username, String title, String type, String date) {
        this.username = username;
        this.title = title;
        this.type = type;
        this.date = date;
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
