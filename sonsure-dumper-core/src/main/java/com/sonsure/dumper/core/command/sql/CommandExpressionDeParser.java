package com.sonsure.dumper.core.command.sql;

import com.sonsure.dumper.core.management.CommandTable;
import com.sonsure.dumper.core.management.MappingCache;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

public class CommandExpressionDeParser extends ExpressionDeParser {

    @Override
    public void visit(Column column) {

        CommandSelectDeParser selectVisitor = (CommandSelectDeParser) getSelectVisitor();
        CommandTableHandler commandTableHandler = selectVisitor.getCommandTableHandler();

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
        //表名实际上是class
        Class<?> entityClass = commandTableHandler.getTableClassForAlias(tableName);

        CommandTable commandTable = new CommandTable();
        commandTable.setModelClass(entityClass);
        String columnName = MappingCache.getColumn(commandTable, column.getColumnName(), commandTableHandler.getMappingHandler());
        getBuffer().append(columnName);
    }
}
