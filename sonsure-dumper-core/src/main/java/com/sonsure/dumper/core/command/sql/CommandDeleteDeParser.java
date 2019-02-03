package com.sonsure.dumper.core.command.sql;

import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.util.deparser.DeleteDeParser;
import net.sf.jsqlparser.util.deparser.LimitDeparser;
import net.sf.jsqlparser.util.deparser.OrderByDeParser;

public class CommandDeleteDeParser extends DeleteDeParser {

    private CommandTableHandler commandTableHandler;

    public CommandDeleteDeParser(ExpressionVisitor expressionVisitor, StringBuilder buffer, CommandTableHandler commandTableHandler) {
        super(expressionVisitor, buffer);
        this.commandTableHandler = commandTableHandler;
    }

    @Override
    public void deParse(Delete delete) {
        getBuffer().append("DELETE");
//        if (delete.getTables() != null && delete.getTables().size() > 0) {
//            for (Table table : delete.getTables()) {
//                getBuffer().append(" ").append(table.getFullyQualifiedName());
//            }
//        }

        Table table = delete.getTable();
        String tableName = this.commandTableHandler.getTableName(table.getName());
        if (table.getAlias() != null) {
            tableName += " " + table.getAlias().toString();
        }

        getBuffer().append(" FROM ").append(tableName);

//        if (delete.getJoins() != null) {
//            for (Join join : delete.getJoins()) {
//                if (join.isSimple()) {
//                    getBuffer().append(", ").append(join);
//                } else {
//                    getBuffer().append(" ").append(join);
//                }
//            }
//        }

        if (delete.getWhere() != null) {
            getBuffer().append(" WHERE ");
            delete.getWhere().accept(getExpressionVisitor());
        }

        if (delete.getOrderByElements() != null) {
            new OrderByDeParser(getExpressionVisitor(), getBuffer()).deParse(delete.getOrderByElements());
        }
        if (delete.getLimit() != null) {
            new LimitDeparser(getBuffer()).deParse(delete.getLimit());
        }
    }
}
