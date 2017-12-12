package service.impl;

import java.sql.SQLException;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import model.Company;
import persistence.ICompanyDao;
import service.ICompanyService;
import service.IComputerService;

@Service("companyService")
@Transactional(readOnly = true)
public class CompanyService implements ICompanyService {

    private ICompanyDao         companyDao;
    private IComputerService    computerService;

    /**
     * Private ctor.
     *
     * @param companyDao CompanyDao to access Data
     * @param computerService computerService to handle transactions
     */
    @Autowired
    private CompanyService(ICompanyDao companyDao, IComputerService computerService) {
        this.companyDao = companyDao;
        this.computerService = computerService;
    }

    /**
     * @return Full company list from DB
     * @throws SQLException content couldn't be loaded
     */
    @Override
    public List<Company> getList() {
        return companyDao.findAll();
    }

    /**
     * @param idCompany idCompany the id to check
     * @return true is company id is present in DB
     * @throws SQLException content couldn't be loaded
     */
    @Override
    public boolean exists(Long idCompany) {
        return companyDao.existsById(idCompany);
    }

    /**
     * Transactional. see applicationContext.xml
     *
     * @param id id to delete
     * @throws SQLException failed to delete
     */
    @Override
    @Transactional(readOnly = false)
    public void delete(Long id) {
        computerService.deleteByCompany(id);
        companyDao.deleteById(id);
    }
}
