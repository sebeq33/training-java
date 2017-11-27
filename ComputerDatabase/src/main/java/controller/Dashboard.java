package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import model.Computer;
import model.pages.Page;
import service.ICompanyService;
import service.IComputerService;
import service.PageRequest;
import validators.ValidationUtils;

@WebServlet
public class Dashboard extends HttpServlet {
    private static final String DASHBOARD_JSP_PATH = "/WEB-INF/pages/dashboard.jsp";
    private static final long serialVersionUID = 1L;

    private IComputerService computerService;
    private ICompanyService companyService;

    /**
     * @param config ServletConfig
     * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
     * @throws ServletException exception
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        WebApplicationContext wc = WebApplicationContextUtils.getWebApplicationContext(getServletContext());
        this.companyService = (ICompanyService) wc.getBean("companyService");
        this.computerService = (IComputerService) wc.getBean("computerService");
    }

    /**
     * {@inheritDoc}
     *
     * @see HttpServlet#doGet(HttpServletRequest req, HttpServletResponse resp)
     * @param req req
     * @param resp resp
     * @throws ServletException req
     * @throws IOException exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        loadDashboard(req, resp);
    }

    /**
     * {@inheritDoc}
     *
     * @see HttpServlet#doPost(HttpServletRequest req, HttpServletResponse resp)
     * @param req req
     * @param resp resp
     * @throws ServletException req
     * @throws IOException exception
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        handleComputerDeletion(req);
        handleCompanyDeletion(req);
        loadDashboard(req, resp);
    }

    /**
     * @param req req
     */
    private void handleCompanyDeletion(HttpServletRequest req) {
        String id = req.getParameter("company_id_delete");

        if (id == null) {
            return;
        }

        if (!ValidationUtils.isLong(id)) {
            RequestUtils.showMsg(req, false, "please provide a company id");
        } else {

            companyService.delete(Long.parseLong(id));
            String msg = "Sucessfully deleted company : n°" + id;
            RequestUtils.showMsg(req, true, msg);
        }
    }

    /**
     * @param req req containing parameters of computers to delete ("selection") or
     *            none (ignored then)
     */
    private void handleComputerDeletion(HttpServletRequest req) {
        String[] computerSelection = req.getParameterValues("computer_selection_delete");

        if (computerSelection == null) {
            return;
        }

        List<Long> ids = new ArrayList<Long>();
        Boolean result = ValidationUtils.isLongList(computerSelection, ids);

        if (result) {
            computerService.delete(ids);
            RequestUtils.showMsg(req, true, "Success, " + ids.size() + " computer ids deleted");
            req.setAttribute("page", 1);
        } else {
            RequestUtils.showMsg(req, false, "all ids are not valid");
        }
    }

    /**
     * @param req req
     * @param resp resp
     * @throws ServletException could not load
     * @throws IOException could not load
     */
    private void loadDashboard(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        PageRequest<Computer> builder = new PageRequest<Computer>();
        Page<Computer> page = computerService.loadPage(builder.with(req));

        if (page != null) {
            buildParams(req, page);
        }

        req.getRequestDispatcher(DASHBOARD_JSP_PATH).forward(req, resp);
    }

    /**
     * @param req req
     * @param page page
     */
    public static void buildParams(HttpServletRequest req, Page<Computer> page) {
        List<Computer> content = page.getContent();
        RequestUtils.buildPageParams(req, page);
        req.setAttribute("computers", content);
        req.setAttribute("page", page);
    }

}
