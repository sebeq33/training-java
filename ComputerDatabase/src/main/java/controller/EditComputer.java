package controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import model.Company;
import model.Computer;
import persistence.exceptions.DaoException;
import service.CompanyServiceImpl;
import service.ComputerServiceImpl;
import service.ICompanyService;
import service.IComputerService;
import validators.ComputerValidator;
import validators.ValidationUtils;

@WebServlet
public class EditComputer extends HttpServlet {

    private static final String COMPUTER_FORM_JSP = "/WEB-INF/pages/computerForm.jsp";
    private static final String ID_IS_NOT_VALID = "id is not valid";
    private static final long serialVersionUID = -7371267190245615780L;
    private static final Logger LOGGER = LoggerFactory.getLogger(Dashboard.class);

    private ICompanyService companyService;
    private IComputerService computerService;

    /**
     * ctor.
     */
    public EditComputer() {
        computerService = ComputerServiceImpl.getInstance();
        companyService = CompanyServiceImpl.getInstance();
    }

    /**
     * @param req current request
     * @param resp response
     * @throws ServletException exception
     * @throws IOException exception
     */
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        if (!loadComputer(req)) {
            resp.sendRedirect("addComputer");
            return;
        }

        loadCompanies(req);
        loadPage(req, resp);
    }

    /**
     * @param req current request
     * @param resp response
     * @throws ServletException exception
     * @throws IOException exception
     */
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {

        String idStr = req.getParameter(ValidationUtils.ID);
        String name = req.getParameter(ValidationUtils.COMPUTER_NAME);
        String introducedStr = req.getParameter(ValidationUtils.INTRODUCED);
        String discontinuedStr = req.getParameter(ValidationUtils.DISCONTINUED);
        String companyIdStr = req.getParameter(ValidationUtils.COMPANY_ID);

        ComputerValidator v = new ComputerValidator();
        if (!v.validate(idStr, name, introducedStr, discontinuedStr, companyIdStr)) {

            StringBuilder sb = new StringBuilder("Computer cannot be edited, ");
            Map<String, String> map = v.getErrors();
            for (Entry<String, String> elem : map.entrySet()) {
                String msg = "reason [" + elem.getKey() + "] : \"" + elem.getValue() + "\"";
                sb.append(msg + "<br/>");
            }
            RequestUtils.showMsg(req, false, sb.toString());
            LOGGER.debug(sb.toString());

        } else {

            Computer c = new Computer(v.getId(), name, v.getIntroduced(), v.getDiscontinued(), v.getCompanyId());
            try {

                computerService.updateComputer(c);
                RequestUtils.showMsg(req, true, "SUCCESS: Computer \"" + name + "\" successfully edited (id=" + v.getId() + ")");

            } catch (DaoException e) {

                String msg = "Computer cannot be edited, reason \"" + e.getMessage() + "\"";
                RequestUtils.showMsg(req, false, msg);
                LOGGER.error(msg);
            }
        }

        RequestUtils.putBackAttributes(req, name, introducedStr, discontinuedStr, companyIdStr);
        loadCompanies(req);
        loadPage(req, resp);
    }

    /**
     * Calls the matching jsp.
     * @param req req
     * @param resp resp
     * @throws ServletException could not be loaded
     * @throws IOException could not be loaded
     */
    private void loadPage(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.setAttribute("edit", true);
        req.getRequestDispatcher(COMPUTER_FORM_JSP).forward(req, resp);
    }

    /**
     * @param req servletRequest
     * @return successfully loaded
     */
    private Boolean loadComputer(HttpServletRequest req) {
        String idStr = req.getParameter("id");

        if (!ValidationUtils.isLong(idStr)) {
            RequestUtils.showMsg(req, false, ID_IS_NOT_VALID);
            return false;
        }

        Long id = Long.parseLong(idStr);
        Computer c;
        try {
            c = computerService.getComputerDetail(id);
            RequestUtils.putBackAttributes(req, id, c.getName(), c.getIntroduced(), c.getDiscontinued(), c.getCompany().getId());
        } catch (DaoException e) {
            RequestUtils.showMsg(req, false, "Computer could not be loaded");
            return false;
        }

        return true;
    }

    /**
     * @param req servletRequest
     */
    private void loadCompanies(HttpServletRequest req) {
        List<Company> companies;

        try {
            companies = companyService.getList();
        } catch (DaoException e) {
            companies = new ArrayList<Company>();
            LOGGER.error("Companies list couldn't be loaded, reason \"" + e.getMessage() + "\"");
        }

        req.setAttribute("companies", companies);
    }

}
