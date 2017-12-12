package model;

import java.time.LocalDate;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import mapper.ComputerMapper;

@Entity
@Table(name = "computer")
public class Computer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ComputerMapper.ID)
    private Long      id;

    @Column(name = ComputerMapper.NAME)
    private String    name;

    @Column(name = ComputerMapper.INTRODUCED)
    private LocalDate introduced;

    @Column(name = ComputerMapper.DISCONTINUED)
    private LocalDate discontinued;

    @ManyToOne(fetch = FetchType.EAGER, cascade = CascadeType.DETACH)
    @JoinColumn(name = "company_id", nullable = true)
    private Company   company;

    /**
     * Default Ctor.
     */
    public Computer() {
    }

    /**
     * @param id unique id
     * @param name name
     * @param introduced introduced
     * @param discontinued discontinued
     * @param company company
     */
    public Computer(Long id, String name, LocalDate introduced, LocalDate discontinued, Company company) {
        this.id = id;
        this.name = name;
        this.introduced = introduced;
        this.discontinued = discontinued;
        this.company = company;
    }

    /**
     * @return String generated from id, name, dates, and companyId, to display in console later
     */
    @Override
    public String toString() {
        return "id=" + id + ", name=\"" + name + "\", introduced=" + introduced + ", discontinued=" + discontinued
                + ", company={" + company.toString() + "}";
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getIntroduced() {
        return introduced;
    }

    public void setIntroduced(LocalDate introduced) {
        this.introduced = introduced;
    }

    public LocalDate getDiscontinued() {
        return discontinued;
    }

    public void setDiscontinued(LocalDate discontinued) {
        this.discontinued = discontinued;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
