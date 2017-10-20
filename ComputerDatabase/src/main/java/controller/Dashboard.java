package controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import dtos.ComputerDto;
import mapper.ComputerMapper;
import mapper.exceptions.PageException;
import model.Computer;
import model.pages.Page;
import persistence.exceptions.DaoException;
import service.ComputerServiceImpl;
import validators.ValidationUtils;

@WebServlet
public class Dashboard extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Long   DEFAULT_PAGESIZE      = 10L;
    private static final Long   DEFAULT_STARTING_PAGE = 1L;
    private static final Logger LOGGER                = LoggerFactory.getLogger(Dashboard.class);
    private ComputerServiceImpl computerService;

    /**
     * Default constructor.
     */
    public Dashboard() {
        computerService = ComputerServiceImpl.getInstance();
    }

    /**
     * {@inheritDoc}
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     * @param request request
     * @param response response
     * @throws ServletException request
     * @throws IOException exception
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        Long pageSize = retrivePageSize(request);
        Long currentPage = ValidationUtils.retrieveLong(request.getParameter("page"), DEFAULT_STARTING_PAGE);

        Page<Computer> page = getPage(currentPage, pageSize);
        List<ComputerDto> computerDtos = new ComputerMapper().createDtos(page.getContent());

        request.setAttribute("computers", computerDtos);
        request.setAttribute("page", page);

        request.getRequestDispatcher("/WEB-INF/dashboard.jsp").forward(request, response);
    }

    /**
     * @param request request containing params and current session to recover current pagination parametered
     * @return the page size to use to paginate current-page
     */
    private Long retrivePageSize(HttpServletRequest request) {
        String param = request.getParameter("pagination");

        if (param != null) {
            Long pagination = ValidationUtils.retrieveLong(param, DEFAULT_PAGESIZE);
            request.getSession().setAttribute("pagination", pagination);
            return pagination;
        }

        Long pagination = (Long) request.getSession().getAttribute("pagination");
        if (pagination != null) {
            request.setAttribute("pagination", pagination);
            return pagination;
        }

        request.getSession().setAttribute("pagination", DEFAULT_PAGESIZE);
        return DEFAULT_PAGESIZE;
    }

    /**
     * @param pageNumber page to get
     * @param pageSize page size
     * @return return the page
     */
    private Page<Computer> getPage(Long pageNumber, Long pageSize) {
        try {

            Page<Computer> computers = computerService.getComputerPage(pageNumber, pageSize);
            computers.load();
            return computers;

        } catch (DaoException | PageException e) {

            String msg = String.format("Computer Page (%d) couldn't be loaded, reason \"%s\"", pageNumber,
                    e.getMessage());
            LOGGER.error(msg);
            return null;

        }
    }


    /**
     * {@inheritDoc}
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     * @param request request
     * @param response response
     * @throws ServletException request
     * @throws IOException exception
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
