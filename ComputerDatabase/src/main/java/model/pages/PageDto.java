package model.pages;

import java.util.List;

import mapper.ComputerMapping;
import service.PageBuilder;
import service.PageUtils;

public class PageDto<T> {

    private List<T>         content;
    private Long            total;

    private Long            pageSize;
    private Long            nbPage;
    private String          search;
    private ComputerMapping columnSort;
    private String          order;

    /**
     * Contruct from a page builder.
     *
     * @param content content to display
     * @param total total of elem
     * @param pageBuilder builder
     */
    public PageDto(List<T> content, Long total, PageBuilder<T> pageBuilder) {
        this(content, total);
        this.nbPage = pageBuilder.getNbPage();
        this.pageSize = pageBuilder.getPageSize();
        this.search = pageBuilder.getSearch();
        this.order = pageBuilder.getOrder();
        this.columnSort = pageBuilder.getColumnSort();
    }

    /**
     * Contruct from an already existing page.
     *
     * @param content content to display
     * @param total total of elem
     * @param page page
     * @param nbPage nbpage (starting a 1)
     */
    public PageDto(List<T> content, Long total, PageDto<T> page, Long nbPage) {
        this(content, total);

        this.nbPage = nbPage;
        this.pageSize = page.getPageSize();
        this.search = page.getSearch();
        this.order = page.getOrder();
        this.columnSort = page.getColumnSort();
    }

    /**
     * Build a page without params (search, sort, order).
     * @param content content to display
     * @param total nb of elem in DB
     * @param pageSize nb of elem / page
     * @param nbPage current page (starting a 1)
     */
    public PageDto(List<T> content, Long total, Long pageSize, Long nbPage) {
        this(content, total);
        this.pageSize = pageSize;
    }

    /**
     * @param content content to display
     * @param total total
     */
    private PageDto(List<T> content, Long total) {
        this.content = content;
        this.total = total;
    }

    /**
     * @return Content or null if not loaded
     */
    public List<T> getContent() {
        return content;
    }

    /**
     * @return true if this page is loaded and there is no next page
     */
    public Boolean hasNext() {
        return !isLoaded() || (nbPage * pageSize < total);
    }

    public Long getTotalCount() {
        return total;
    }

    public Boolean isLoaded() {
        return content != null;
    }

    public Long getPageSize() {
        return pageSize;
    }

    public Long getCurrentPage() {
        return nbPage;
    }

    /**
     * @return the number of page that can be loaded with pageSize element per page
     *         (with imcomplete page)
     */
    public Long getTotalPages() {
        return PageUtils.getLimit(this.pageSize, this.total);
    }

    public String getSearch() {
        return search;
    }

    public String getFormSort() {
        return columnSort != null ? columnSort.getFormName() : null;
    }

    public String getDbSort() {
        return columnSort != null ? columnSort.getDbName() : null;
    }

    private ComputerMapping getColumnSort() {
        return columnSort;
    }

    public String getOrder() {
        return order;
    }

}
