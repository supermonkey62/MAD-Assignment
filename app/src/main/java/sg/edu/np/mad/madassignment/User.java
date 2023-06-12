package sg.edu.np.mad.madassignment;

public class User {
    private String name, description;
    private int id;
    private boolean followed;

    public User(String name, String description, int id, boolean followed) {
        this.name = name;
        this.description = description;
        this.id = id;
        this.followed = followed;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getId() { return id; }
    public boolean isFollowed() { return followed; }

    public void setName(String name) { this.name = name; }
    public void setDescription(String description) { this.description = description; }
    public void setId(int id) { this.id = id; }
    public void setFollowed(boolean followed) { this.followed = followed; }

    public boolean isFollowed(User user) {
        return user.isFollowed();
    }
}
