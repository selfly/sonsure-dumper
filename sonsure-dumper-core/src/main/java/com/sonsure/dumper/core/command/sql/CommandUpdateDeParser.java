/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.sql;

import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.LimitDeparser;
import net.sf.jsqlparser.util.deparser.OrderByDeParser;
import net.sf.jsqlparser.util.deparser.UpdateDeParser;

import java.util.Iterator;
import java.util.List;

public class CommandUpdateDeParser extends UpdateDeParser {

    private SelectVisitor selectVisitor;
    private CommandMappingHandler commandMappingHandler;

    public CommandUpdateDeParser(ExpressionVisitor expressionVisitor, SelectVisitor selectVisitor, StringBuilder buffer, CommandMappingHandler commandMappingHandler) {
        super(expressionVisitor, selectVisitor, buffer);
        this.selectVisitor = selectVisitor;
        this.commandMappingHandler = commandMappingHandler;
    }

    @Override
    public void deParse(Update update) {

        getBuffer().append("UPDATE ").append(this.getStringList(update.getTables(), true, false)).
                append(" SET ");

        if (!update.isUseSelect()) {
            for (int i = 0; i < update.getColumns().size(); i++) {
                Column column = update.getColumns().get(i);
                column.accept(getExpressionVisitor());

                getBuffer().append(" = ");

                Expression expression = update.getExpressions().get(i);
                expression.accept(getExpressionVisitor());
                if (i < update.getColumns().size() - 1) {
                    getBuffer().append(", ");
                }
            }
        } else {
            if (update.isUseColumnsBrackets()) {
                getBuffer().append("(");
            }
            for (int i = 0; i < update.getColumns().size(); i++) {
                if (i != 0) {
                    getBuffer().append(", ");
                }
                Column column = update.getColumns().get(i);
                column.accept(getExpressionVisitor());
            }
            if (update.isUseColumnsBrackets()) {
                getBuffer().append(")");
            }
            getBuffer().append(" = ");
            getBuffer().append("(");
            Select select = update.getSelect();
            select.getSelectBody().accept(selectVisitor);
            getBuffer().append(")");
        }

        if (update.getFromItem() != null) {
            getBuffer().append(" FROM ").append(update.getFromItem());
            if (update.getJoins() != null) {
                for (Join join : update.getJoins()) {
                    if (join.isSimple()) {
                        getBuffer().append(", ").append(join);
                    } else {
                        getBuffer().append(" ").append(join);
                    }
                }
            }
        }

        if (update.getWhere() != null) {
            getBuffer().append(" WHERE ");
            update.getWhere().accept(getExpressionVisitor());
        }
        if (update.getOrderByElements() != null) {
            new OrderByDeParser(getExpressionVisitor(), getBuffer()).deParse(update.getOrderByElements());
        }
        if (update.getLimit() != null) {
            new LimitDeparser(getBuffer()).deParse(update.getLimit());
        }

        if (update.isReturningAllColumns()) {
            getBuffer().append(" RETURNING *");
        } else if (update.getReturningExpressionList() != null) {
            getBuffer().append(" RETURNING ");
            for (Iterator<SelectExpressionItem> iter = update.getReturningExpressionList().
                    iterator(); iter.hasNext(); ) {
                getBuffer().append(iter.next().toString());
                if (iter.hasNext()) {
                    getBuffer().append(", ");
                }
            }
        }
    }

    public String getStringList(List<Table> list, boolean useComma, boolean useBrackets) {
        StringBuilder ans = new StringBuilder();
        String comma = ",";
        if (!useComma) {
            comma = "";
        }
        if (list != null) {
            if (useBrackets) {
                ans.append("(");
            }

            for (int i = 0; i < list.size(); i++) {
                Table table = list.get(i);
                String tableName = this.commandMappingHandler.getTableName(table);
                if (table.getAlias() != null) {
                    tableName += " " + table.getAlias().toString();
                }
                ans.append(tableName).append((i < list.size() - 1) ? comma + " " : "");
            }

            if (useBrackets) {
                ans.append(")");
            }
        }

        return ans.toString();
    }
}
