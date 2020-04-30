/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.page;

import com.sonsure.commons.model.Pagination;
import org.apache.commons.lang3.StringUtils;

public class MysqlPageHandler extends AbstractPageHandler {

    @Override
    public boolean support(String dialect) {
        return StringUtils.indexOfIgnoreCase(dialect, "mysql") != -1;
    }

    @Override
    public String getPageCommand(String command, Pagination pagination, String dialect) {
        StringBuilder pageSql = new StringBuilder(200);
        pageSql.append(command);
        pageSql.append(" limit ");
        pageSql.append(pagination.getBeginIndex());
        pageSql.append(",");
        pageSql.append(pagination.getPageSize());
        return pageSql.toString();
    }
}
