/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.sql;

import com.sonsure.dumper.core.persist.KeyGenerator;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import org.apache.commons.lang3.StringUtils;

public class CommandExpressionDeParser extends ExpressionDeParser {

    @Override
    public void visit(Column column) {

        CommandSelectDeParser selectVisitor = (CommandSelectDeParser) getSelectVisitor();
        CommandMappingHandler commandMappingHandler = selectVisitor.getCommandMappingHandler();

        final Table table = column.getTable();
        String tableName = null;
        if (table != null) {
            if (table.getAlias() != null) {
                tableName = table.getAlias().getName();
            } else {
                tableName = table.getFullyQualifiedName();
            }
        }
        if (tableName != null && !tableName.isEmpty()) {
            getBuffer().append(tableName).append(".");
        }
        String columnName = column.getColumnName();
        if (StringUtils.startsWith(columnName, KeyGenerator.NATIVE_OPEN_TOKEN) && StringUtils.endsWith(columnName, KeyGenerator.NATIVE_CLOSE_TOKEN)) {
            columnName = StringUtils.substring(columnName, KeyGenerator.NATIVE_OPEN_TOKEN.length(), columnName.length() - KeyGenerator.NATIVE_CLOSE_TOKEN.length());
        } else {
            columnName = commandMappingHandler.getColumnName(column);
        }
        getBuffer().append(columnName);
    }
}
