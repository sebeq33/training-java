package service;

import javax.servlet.http.HttpServletRequest;

import mapper.ComputerMapping;
import model.pages.Order;
import model.pages.Page;
import persistence.querycommands.PageQuery;
import validators.ValidationUtils;

public class PageRequest<T> {
    private static final Long DEFAULT_PAGESIZE      = 20L;
    private static final Long DEFAULT_STARTING_PAGE = 1L;

    private Long              pageSize;
    private Long              nbPage;
    private String            search;
    private ComputerMapping   columnSort;
    private String            order;

    /**
     * default ctor.
     */
    public PageRequest() {
    }

    /**
     * @param req req
     * @return this
     */
    public PageRequest<T> with(HttpServletRequest req) {
        Long pageSize = ValidationUtils.retrieveLong(req.getParameter("pagination"), DEFAULT_PAGESIZE);
        Long pageNumber = ValidationUtils.retrieveLong(req.getParameter("page"), DEFAULT_STARTING_PAGE);
        this.atPage(pageNumber);
        this.withPageSize(pageSize);
        this.withSearch(req.getParameter("search"));
        this.withSort(req.getParameter("sort"));
        this.withOrder(req.getParameter("order"));
        return this;
    }

    /**
     * @param parameter order as String ASC/DESC
     * @return this
     */
    private PageRequest<T> withOrder(String parameter) {
        if (parameter == null || parameter.isEmpty()) {
            return this;
        }
        Order o = parameter.equals(Order.ASC.toString()) ? Order.ASC : Order.DESC;
        this.order = o.toString();
        return this;
    }

    /**
     * @param parameter sort column (see ComputerMapper)
     * @return this
     */
    private PageRequest<T> withSort(String parameter) {
        if (parameter == null || parameter.isEmpty()) {
            return this;
        }
        this.columnSort = ComputerMapping.get(parameter);
        return this;
    }

    /**
     * @param parameter search String
     * @return this
     */
    private PageRequest<T> withSearch(String parameter) {
        if (parameter == null || parameter.isEmpty()) {
            return this;
        }

        this.search = parameter;
        return this;
    }

    /**
     * @param pageSize pageSize
     * @return this
     */
    private PageRequest<T> withPageSize(Long pageSize) {
        this.pageSize = Math.min(pageSize, 150L);
        return this;
    }

    /**
     * @param pageNumber pageNumber
     * @return this
     */
    private PageRequest<T> atPage(Long pageNumber) {
        this.nbPage = pageNumber;
        return this;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public Long getNbPage() {
        return nbPage;
    }

    public String getSearch() {
        return search;
    }

    public ComputerMapping getColumnSort() {
        return columnSort;
    }

    public String getOrder() {
        return order;
    }

    /**
     * @param pageQuery query used to retrieve content
     * @param size total number of element
     * @return the resulting page
     */
    public Page<T> build(PageQuery<T> pageQuery, Long size) {
        Long firstEntityIndex = PageUtils.getFirstEntityIndex(this.nbPage, this.pageSize);
        if (firstEntityIndex >= size) {
            this.nbPage = size / this.pageSize;
        }
        return new Page<T>(pageQuery, size, this);
    }
}
