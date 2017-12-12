package persistence;

import org.springframework.data.jpa.repository.JpaRepository;

import model.Company;

public interface ICompanyDao extends JpaRepository<Company, Long> {

}