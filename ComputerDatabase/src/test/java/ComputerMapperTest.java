import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import mapper.ComputerMapper;
import model.Computer;

@RunWith(MockitoJUnitRunner.class)
public class ComputerMapperTest {
    private ResultSet resultSet;

    private ComputerMapper mapper;

    /**
     * Init Mocks.
     * @throws SQLException never thrown
     */
    @Before
    public void setUp() throws SQLException {
        mapper = new ComputerMapper();
    }

    /**
     * @throws SQLException never thrown
     */
    @Test
    public void processTest() throws SQLException {
        Long id = 10L;
        String host = "host";
        LocalDate date = getDate();
        Long companyId = 42L;
        String companyName = "cname";

        resultSet = Mockito.mock(ResultSet.class);
        Mockito.when(resultSet.getLong(ComputerMapper.ID)).thenReturn(id);
        Mockito.when(resultSet.getString(ComputerMapper.NAME)).thenReturn(host);
        Mockito.when(resultSet.getDate(ComputerMapper.INTRODUCED)).thenReturn(null);
        Mockito.when(resultSet.getDate(ComputerMapper.DISCONTINUED)).thenReturn(Date.valueOf(date));
        Mockito.when(resultSet.getLong(ComputerMapper.COMPANY_ID)).thenReturn(companyId);
        Mockito.when(resultSet.getString(ComputerMapper.COMPANY_NAME)).thenReturn(companyName);
        Mockito.when(resultSet.next()).thenReturn(true).thenReturn(false);

        Computer result =  mapper.mapRow(resultSet, 0);

        assertEquals(id.longValue(), result.getId().longValue());
        assertEquals(host, result.getName());
        assertEquals(null, result.getIntroduced());
        assertEquals(date, result.getDiscontinued());
        assertEquals(companyId.longValue(), result.getCompany().getId().longValue());
        assertEquals(companyName, result.getCompany().getName());
    }

    /**
     * @return just a sql.Date mocked
     */
    private LocalDate getDate() {
        return LocalDate.parse("27-07-2017", DateTimeFormatter.ofPattern("dd-MM-yyyy"));
    }

}
