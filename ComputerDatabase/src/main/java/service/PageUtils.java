package service;

import model.pages.PageDto;

public class PageUtils {

    /**
     * @param page loaded page
     * @return concatenation of each .toString() loaded content
     */
    public static String dump(PageDto<?> page) {
        StringBuilder b = new StringBuilder();
        for (Object t : page.getContent()) {
            b.append(t.toString());
            b.append(System.lineSeparator());
        }
        return b.toString();
    }

    /**
     * @param pageSize pageSize
     * @param total total of elem/entities count
     * @return return the last possible page
     */
    public static Long getLimit(Long pageSize, Long total) {
        return (total + pageSize - 1) / pageSize;
    }
}
