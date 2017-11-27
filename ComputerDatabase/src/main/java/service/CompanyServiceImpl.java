package service;

import java.sql.Connection;
import java.util.List;

import model.Company;
import model.pages.Page;
import persistence.CompanyDaoImpl;
import persistence.Transaction;
import persistence.exceptions.DaoException;

public class CompanyServiceImpl implements ICompanyService {


    private static CompanyServiceImpl instance;
    private CompanyDaoImpl companyDao;

    /**
     * Private ctor.
     *
     * @param dao CompanyDao to access Data
     */
    private CompanyServiceImpl(CompanyDaoImpl dao) {
        companyDao = dao;
    }

    /**
     * @return a loaded Service, ready to work
     */
    public static ICompanyService getInstance() {
        if (instance == null) {
            instance = new CompanyServiceImpl(CompanyDaoImpl.getInstance());
        }
        return instance;
    }

    /**
     * @return Full company list from DB
     * @throws DaoException content couldn't be loaded
     */
    @Override
    public List<Company> getList() throws DaoException {
        return companyDao.getCompanyList();
    }

    /**
     * @param idCompany idCompany the id to check
     * @return true is company id is present in DB
     * @throws DaoException content couldn't be loaded
     */
    @Override
    public boolean exists(Long idCompany) throws DaoException {
        if (idCompany == null) {
            throw new NullPointerException();
        }
        return companyDao.companyExists(idCompany);
    }

    /**
     * @return the first page of the full company list from DB
     * @throws DaoException content couldn't be loaded
     */
    @Override
    public Page<Company> getPage() throws DaoException {

        return companyDao.getCompanyPage();
    }

    /**
     * @param id id to delete
     * @throws DaoException failed to delete
     */
    @Override
    public void delete(Long id) throws DaoException {
        Connection conn = Transaction.openTransaction();

        ComputerServiceImpl.getInstance().deleteComputerByCompany(id);
        companyDao.deleteCompany(id);

        Transaction.releaseTransaction(conn);
    }
}
