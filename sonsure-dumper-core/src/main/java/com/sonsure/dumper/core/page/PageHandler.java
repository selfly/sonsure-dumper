package com.sonsure.dumper.core.page;


import com.sonsure.commons.model.Pagination;

/**
 * Created by liyd on 16/6/8.
 */
public interface PageHandler {

    /**
     * 是否支持
     *
     * @param dialect
     * @return
     */
    boolean support(String dialect);

    /**
     * 根据查询语句获取查count语句
     *
     * @param command the command
     * @param dialect the dialect
     * @return count command
     */
    String getCountCommand(String command, String dialect);

    /**
     * 根据查询语句获取分页语句
     *
     * @param command    the command
     * @param pagination the pagination
     * @param dialect    the dialect
     * @return page command
     */
    String getPageCommand(String command, Pagination pagination, String dialect);
}
