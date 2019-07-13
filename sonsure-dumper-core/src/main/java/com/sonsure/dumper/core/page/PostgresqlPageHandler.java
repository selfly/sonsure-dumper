package com.sonsure.dumper.core.page;

import com.sonsure.commons.model.Pagination;
import org.apache.commons.lang3.StringUtils;

public class PostgresqlPageHandler extends AbstractPageHandler {

    @Override
    public boolean support(String dialect) {
        return StringUtils.indexOfIgnoreCase(dialect, "postgresql") != -1;
    }

    @Override
    public String getPageCommand(String command, Pagination pagination, String dialect) {
        StringBuilder pageSql = new StringBuilder(200);
        pageSql.append(command);
        pageSql.append(" limit ");
        pageSql.append(pagination.getPageSize());
        pageSql.append(" offset ");
        pageSql.append(pagination.getBeginIndex());
        return pageSql.toString();
    }
}
