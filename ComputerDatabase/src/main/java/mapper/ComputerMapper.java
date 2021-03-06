package mapper;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import org.springframework.jdbc.core.RowMapper;

import model.Company;
import model.Computer;

public class ComputerMapper implements RowMapper<Computer> {

    public static final String COMPANY_NAME = "company_name";
    public static final String COMPANY_ID   = "CO.company_id";
    public static final String DISCONTINUED = "CO.discontinued";
    public static final String INTRODUCED   = "CO.introduced";
    public static final String NAME         = "CO.name";
    public static final String ID           = "CO.id";

    /**
     * @param rs one computer loaded from DB
     * @param i index
     * @return the computer details
     * @throws SQLException an unexpected error occur while accessing datas
     */
    @Override
    public Computer mapRow(ResultSet rs, int i) throws SQLException {

        Long id = rs.getLong(ID);
        String name = rs.getString(NAME);
        Date introducedDate = rs.getDate(INTRODUCED);
        LocalDate introduced = introducedDate != null ? introducedDate.toLocalDate() : null;
        Date discontinuedDate = rs.getDate(DISCONTINUED);
        LocalDate discontinued = discontinuedDate != null ? discontinuedDate.toLocalDate() : null;

        Long idCompany = rs.getLong(COMPANY_ID);
        String nameCompany = rs.getString(COMPANY_NAME);
        Company company = new Company(idCompany == 0 ? null : idCompany, nameCompany);

        return new Computer(id, name, introduced, discontinued, company);
    }

}
