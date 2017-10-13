package model;

public class Company {
    private Long   id;
    private String name;

    /**
     * @param name Company label
     */
    public Company(String name) {
        this.name = name;
    }

    /**
     * @param id unique id to this company
     * @param name Company label
     */
    public Company(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return String generated from id and name, to display in console later
     */
    @Override
    public String toString() {
        return "CompanyModel [id=" + id + ", name=" + name + "]";
    }
}