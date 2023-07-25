package sg.edu.np.mad.team5MADAssignmentOnTask;

public class Task {

    String username, title, date,tag,category;
    Boolean Status,archive;

    float timespent;
    int sessions;



   public Task(){};

    public Task(String username, String title, String date,String tag,Boolean status,float timespent, int sessions,String category,boolean archive){
        this.username = username;
        this.title = title;
        this.date = date;
        this.tag = tag;
        this.Status = status;
        this.timespent = timespent;
        this.sessions = sessions;
        this.category = category;
        this.archive = archive;

    }

    public Boolean getArchive() {
        return archive;
    }

    public void setArchive(Boolean archive) {
        this.archive = archive;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
