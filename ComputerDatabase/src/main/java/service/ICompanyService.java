package service;

import java.util.List;

import model.Company;

public interface ICompanyService {

    /**
     * @return Full company list from DB
     */
    List<Company> getList();

    /**
     * @param idCompany idCompany the id to check
     * @return true is company id is present in DB
     */
    boolean exists(Long idCompany);

    /**
     * @param id id to delete
     */
    void delete(Long id);
}