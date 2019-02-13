package com.sonsure.dumper.core.command.sql;

import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;

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

        String columnName = commandMappingHandler.getColumnName(column);
        getBuffer().append(columnName);
    }
}
