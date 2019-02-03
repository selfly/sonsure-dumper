package com.sonsure.dumper.core.page;

import com.sonsure.commons.model.Pagination;
import org.apache.commons.lang3.StringUtils;

public class OraclePageHandler extends AbstractPageHandler {

    @Override
    public boolean support(String dialect) {
        return StringUtils.indexOfIgnoreCase(dialect, "oracle") != -1;
    }

    @Override
    public String getPageCommand(String command, Pagination pagination, String dialect) {
        StringBuilder pageSql = new StringBuilder(200);
        pageSql.append("select * from ( select rownum _num,_temp.* from (");
        pageSql.append(command);
        pageSql.append(") _temp where rownum <= ").append(pagination.getEndIndex());
        pageSql.append(") where _num > ").append(pagination.getBeginIndex());
        return pageSql.toString();
    }
}
