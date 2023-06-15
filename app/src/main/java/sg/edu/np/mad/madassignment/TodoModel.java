package sg.edu.np.mad.madassignment;

public class TodoModel {
    private int id;
    private boolean status;
    private String task;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public TodoModel() {
    }

    public TodoModel(int id, boolean status, String task) {
        this.id = id;
        this.status = status;
        this.task = task;
    }
}
