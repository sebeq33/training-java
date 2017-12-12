package model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "company")
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long   id = null;

    @Column(name = "name")
    private String name = null;

    /**
     * Default ctor.
     */
    public Company() {

    }

    /**
     * @param id unique id to this company
     * @param name Company label
     */
    public Company(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public Long getId() {
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
