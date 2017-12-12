package service;

import java.util.List;

import mapper.ComputerMapping;
import model.pages.Order;
import model.pages.PageDto;

public class PageBuilder<T> {

    private static final Long DEFAULT_PAGESIZE      = 20L;
    private static final Long DEFAULT_STARTING_PAGE = 1L;

    private Long              pageSize = DEFAULT_PAGESIZE;
    private Long              nbPage = DEFAULT_STARTING_PAGE;
    private String            search;
    private ComputerMapping   columnSort;
    private String            order;

    /**
     * default ctor.
     */
    public PageBuilder() {
    }

    /**
     * @param parameter order as String ASC/DESC
     * @return this
     */
    public PageBuilder<T> withOrder(String parameter) {
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
    public PageBuilder<T> withSort(String parameter) {
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
    public PageBuilder<T> withSearch(String parameter) {
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
    public PageBuilder<T> withPageSize(Long pageSize) {
        this.pageSize = Math.min(pageSize, 100L);
        return this;
    }

    /**
     * @param pageNumber pageNumber
     * @return this
     */
    public PageBuilder<T> atPage(Long pageNumber) {
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
     * @param content loaded Content
     * @param size total number of element
     * @return the resulting page
     */
    public PageDto<T> build(List<T> content, Long size) {
        return new PageDto<T>(content, size, this);
    }
}
