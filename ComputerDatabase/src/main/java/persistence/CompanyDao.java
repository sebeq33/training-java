package persistence;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import mapper.CompanyMapper;
import mapper.exceptions.PageException;
import mapper.pages.Page;
import model.Company;
import persistence.exceptions.DaoException;
import persistence.querycommands.PageQueryCommand;

public class CompanyDao {
    private static final String SELECT_COUNT_FROM_COMPANY          = "select count(*) from company";
    private static final String SELECT_COUNT_FROM_COMPANY_WHERE_ID = "select count(*) from company where id = ?";
    private static final String SELECT_ID_NAME_FROM_COMPANY        = "select id, name from company";
    private static CompanyDao   instance;

    /**
     * private ctor.
     */
    private CompanyDao() {
    }

    /**
     * @return unique dao instance.
     */
    public static CompanyDao getInstance() {
        if (instance == null) {
            instance = new CompanyDao();
        }
        return instance;
    }

    /**
     * @return Full company list from DB
     * @throws DaoException content couldn't be loaded
     */
    public List<Company> getCompanyList() throws DaoException {
        return DaoConnection.executeSelectQuery(SELECT_ID_NAME_FROM_COMPANY, new CompanyMapper());
    }

    /**
     * @param idCompany the id to check
     * @return true is company id is present in DB
     * @throws DaoException content couldn't be loaded
     */
    public boolean companyExists(Long idCompany) throws DaoException {
        return DaoConnection.executeQuery((Connection conn) -> {
            try (PreparedStatement s = conn.prepareStatement(SELECT_COUNT_FROM_COMPANY_WHERE_ID)) {
                s.setLong(1, idCompany);

                try (ResultSet r = s.executeQuery()) {
                    return (r.next() ? r.getLong(1) > 0 : false);
                }
            }
        });
    }

    /**
     * @return the first page of the full company list from DB
     * @throws DaoException content couldn't be loaded
     */
    public Page<Company> getCompanyPage() throws DaoException {
        Long size = getCompanyCount();

        PageQueryCommand<Company> command = (Long start, Long splitSize) -> {
            String filter = String.format(" ORDER BY id LIMIT %d,%d", start, splitSize);
            try {
                return DaoConnection.executeSelectQuery(SELECT_ID_NAME_FROM_COMPANY + filter, new CompanyMapper());
            } catch (DaoException e) {
                throw new PageException(e);
            }
        };

        return new Page<Company>(command, size);
    }

    /**
     * @return the number of company in DB
     * @throws DaoException content couldn't be loaded
     */
    public Long getCompanyCount() throws DaoException {
        Long size = DaoConnection.executeSelectQuery(SELECT_COUNT_FROM_COMPANY, (ResultSet r) -> {
            return (r.next() ? r.getLong(1) : null);
        });
        return size;
    }
}
