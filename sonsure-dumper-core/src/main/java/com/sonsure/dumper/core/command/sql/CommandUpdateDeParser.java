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
import net.sf.jsqlparser.statement.select.Join;
import net.sf.jsqlparser.statement.select.Select;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.LimitDeparser;
import net.sf.jsqlparser.util.deparser.OrderByDeParser;
import net.sf.jsqlparser.util.deparser.UpdateDeParser;

import java.util.Iterator;

/**
 * @author liyd
 */
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
        String tableName = this.commandMappingHandler.getTableName(update.getTable());
        buffer.append("UPDATE ").append(tableName);
        if (update.getStartJoins() != null) {
            for (Join join : update.getStartJoins()) {
                if (join.isSimple()) {
                    buffer.append(", ").append(join);
                } else {
                    buffer.append(" ").append(join);
                }
            }
        }
        buffer.append(" SET ");

        if (!update.isUseSelect()) {
            for (int i = 0; i < update.getColumns().size(); i++) {
                Column column = update.getColumns().get(i);
                column.accept(getExpressionVisitor());

                buffer.append(" = ");

                Expression expression = update.getExpressions().get(i);
                expression.accept(getExpressionVisitor());
                if (i < update.getColumns().size() - 1) {
                    buffer.append(", ");
                }
            }
        } else {
            if (update.isUseColumnsBrackets()) {
                buffer.append("(");
            }
            for (int i = 0; i < update.getColumns().size(); i++) {
                if (i != 0) {
                    buffer.append(", ");
                }
                Column column = update.getColumns().get(i);
                column.accept(getExpressionVisitor());
            }
            if (update.isUseColumnsBrackets()) {
                buffer.append(")");
            }
            buffer.append(" = ");
            buffer.append("(");
            Select select = update.getSelect();
            select.getSelectBody().accept(selectVisitor);
            buffer.append(")");
        }

        if (update.getFromItem() != null) {
            buffer.append(" FROM ").append(update.getFromItem());
            if (update.getJoins() != null) {
                for (Join join : update.getJoins()) {
                    if (join.isSimple()) {
                        buffer.append(", ").append(join);
                    } else {
                        buffer.append(" ").append(join);
                    }
                }
            }
        }

        if (update.getWhere() != null) {
            buffer.append(" WHERE ");
            update.getWhere().accept(getExpressionVisitor());
        }
        if (update.getOrderByElements() != null) {
            new OrderByDeParser(getExpressionVisitor(), buffer).deParse(update.getOrderByElements());
        }
        if (update.getLimit() != null) {
            new LimitDeparser(buffer).deParse(update.getLimit());
        }

        if (update.isReturningAllColumns()) {
            buffer.append(" RETURNING *");
        } else if (update.getReturningExpressionList() != null) {
            buffer.append(" RETURNING ");
            for (Iterator<SelectExpressionItem> iter = update.getReturningExpressionList().
                    iterator(); iter.hasNext(); ) {
                buffer.append(iter.next().toString());
                if (iter.hasNext()) {
                    buffer.append(", ");
                }
            }
        }
    }
}
