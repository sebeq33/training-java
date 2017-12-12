import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import model.Computer;
import model.pages.PageDto;
import service.PageBuilder;

@RunWith(MockitoJUnitRunner.class)
public class PageResquestTest {

    /**
     * Page size cannot be > 100.
     */
    @Test
    public void pageAndpageSizeDefault() {
        PageBuilder<Computer> r = new PageBuilder<Computer>();

        assertNotNull(r.getPageSize());
        assertNotSame(0L, r.getPageSize());

        assertNotNull(r.getNbPage());
        assertNotSame(0L, r.getNbPage());
    }

    /**
     * Page size cannot be > 100.
     */
    @Test
    public void pageSizeLimit() {
        PageBuilder<Computer> r = new PageBuilder<Computer>();

        r.withPageSize(10L);
        assertSame(10L, r.getPageSize());

        r.withPageSize(100L);
        assertSame(100L, r.getPageSize());

        r.withPageSize(300L);
        assertSame(100L, r.getPageSize());
    }

    /**
     * build page using pageRequest.
     * @throws SQLException impossible
     */
    @Test
    public void buildNormal() throws SQLException {
        List<Computer> asList = Arrays.asList(Mockito.mock(Computer.class));
        PageBuilder<Computer> r = new PageBuilder<Computer>();
        PageDto<Computer> pageResult = r.build(asList, 10L);
        assertSame(10L, pageResult.getTotalCount());

        assertSame(null, pageResult.getContent());
        assertSame(asList, pageResult.getContent());
    }

}
