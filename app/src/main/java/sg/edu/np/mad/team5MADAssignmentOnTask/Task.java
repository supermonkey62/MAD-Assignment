package sg.edu.np.mad.team5MADAssignmentOnTask;

public class Task {

    String username, title, type, date,tag,category;
    Boolean Status;

    float timespent;
    int sessions;


    public Task() {
    }

    public Task(String username, String title, String type, String date,String tag,Boolean status,float timespent, int sessions,String category){
        this.username = username;
        this.title = title;
        this.type = type;
        this.date = date;
        this.tag = tag;
        this.Status = status;
        this.timespent = timespent;
        this.sessions = sessions;
        this.category = category;

    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getSessions() {
        return sessions;
    }

    public void setSessions(int sessions) {
        this.sessions = sessions;
    }

    public float getTimespent() {
        return timespent;
    }

    public void setTimespent(float timespent) {
        this.timespent = timespent;
    }

    public Boolean getStatus() {
        return Status;
    }

    public void setStatus(Boolean status) {
        this.Status = status;
    }

    public String getTag(){return tag;}
    public void setTag(String tag){this.tag = tag;}

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
