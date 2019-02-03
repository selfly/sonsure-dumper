package com.sonsure.dumper.core.page;

import com.sonsure.commons.model.Pagination;
import com.sonsure.dumper.core.exception.SonsureJdbcException;

import java.util.ArrayList;
import java.util.List;

public class NegotiatingPageHandler implements PageHandler {

    protected List<PageHandler> defaultPageHandlers;

    protected List<PageHandler> pageHandlers;

    public NegotiatingPageHandler() {
        defaultPageHandlers = new ArrayList<>();
        defaultPageHandlers.add(new MysqlPageHandler());
        defaultPageHandlers.add(new OraclePageHandler());
    }

    @Override
    public boolean support(String dialect) {
        return true;
    }

    @Override
    public String getCountCommand(String command, String dialect) {

        return getPageHandler(dialect).getCountCommand(command, dialect);
    }

    @Override
    public String getPageCommand(String command, Pagination pagination, String dialect) {

        return getPageHandler(dialect).getPageCommand(command, pagination, dialect);
    }

    /**
     * 根据dialect获取对应的pageHandler
     *
     * @param dialect
     * @return
     */
    protected PageHandler getPageHandler(String dialect) {
        for (PageHandler defaultPageHandler : defaultPageHandlers) {
            if (defaultPageHandler.support(dialect)) {
                return defaultPageHandler;
            }
        }
        if (pageHandlers != null) {
            for (PageHandler pageHandler : pageHandlers) {
                if (pageHandler.support(dialect)) {
                    return pageHandler;
                }
            }
        }
        throw new SonsureJdbcException("当前数据库dialect:" + dialect + "没有适配的PageHandler");
    }
}
