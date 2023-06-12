package sg.edu.np.mad.madassignment;

public class Trial {
    private String name;
    private Integer Id;


    public Trial() {
    }

    public Trial(String name, Integer id) {
        this.name = name;
        Id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
    }
}
