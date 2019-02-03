package com.sonsure.dumper.core.command.sql;

import com.sonsure.commons.utils.ClassUtils;
import com.sonsure.dumper.core.management.CommandTable;
import com.sonsure.dumper.core.management.MappingCache;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.schema.Column;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.select.SelectExpressionItem;
import net.sf.jsqlparser.statement.select.SelectVisitor;
import net.sf.jsqlparser.statement.select.WithItem;
import net.sf.jsqlparser.util.deparser.InsertDeParser;

import java.util.Iterator;

public class CommandInsertDeParser extends InsertDeParser {

    private CommandTableHandler commandTableHandler;

    public CommandInsertDeParser(ExpressionVisitor expressionVisitor, SelectVisitor selectVisitor, StringBuilder buffer, CommandTableHandler commandTableHandler) {
        super(expressionVisitor, selectVisitor, buffer);
        this.commandTableHandler = commandTableHandler;
    }


    public void deParse(Insert insert) {
        getBuffer().append("INSERT ");
        if (insert.getModifierPriority() != null) {
            getBuffer().append(insert.getModifierPriority()).append(" ");
        }
        if (insert.isModifierIgnore()) {
            getBuffer().append("IGNORE ");
        }
        getBuffer().append("INTO ");

        String commandTbName = insert.getTable().getName();
        String tableName = commandTableHandler.getTableName(commandTbName);
        getBuffer().append(tableName);

        if (insert.getColumns() != null) {

            //表名实际上是class
            Class<?> entityClass = ClassUtils.loadClass(commandTableHandler.getTableClass(commandTbName));
            CommandTable commandTable = new CommandTable();
            commandTable.setModelClass(entityClass);

            getBuffer().append(" (");
            for (Iterator<Column> iter = insert.getColumns().iterator(); iter.hasNext(); ) {
                Column column = iter.next();
                String columnName = MappingCache.getColumn(commandTable, column.getColumnName(), commandTableHandler.getMappingHandler());
                getBuffer().append(columnName);
                if (iter.hasNext()) {
                    getBuffer().append(", ");
                }
            }
            getBuffer().append(")");
        }

        if (insert.getItemsList() != null) {
            insert.getItemsList().accept(this);
        }

        if (insert.getSelect() != null) {
            getBuffer().append(" ");
            if (insert.isUseSelectBrackets()) {
                getBuffer().append("(");
            }
            if (insert.getSelect().getWithItemsList() != null) {
                getBuffer().append("WITH ");
                for (WithItem with : insert.getSelect().getWithItemsList()) {
                    with.accept(getSelectVisitor());
                }
                getBuffer().append(" ");
            }
            insert.getSelect().getSelectBody().accept(getSelectVisitor());
            if (insert.isUseSelectBrackets()) {
                getBuffer().append(")");
            }
        }

        if (insert.isUseDuplicate()) {
            getBuffer().append(" ON DUPLICATE KEY UPDATE ");
            for (int i = 0; i < insert.getDuplicateUpdateColumns().size(); i++) {
                Column column = insert.getDuplicateUpdateColumns().get(i);
                getBuffer().append(column.getFullyQualifiedName()).append(" = ");

                Expression expression = insert.getDuplicateUpdateExpressionList().get(i);
                expression.accept(getExpressionVisitor());
                if (i < insert.getDuplicateUpdateColumns().size() - 1) {
                    getBuffer().append(", ");
                }
            }
        }

        if (insert.isReturningAllColumns()) {
            getBuffer().append(" RETURNING *");
        } else if (insert.getReturningExpressionList() != null) {
            getBuffer().append(" RETURNING ");
            for (Iterator<SelectExpressionItem> iter = insert.getReturningExpressionList().
                    iterator(); iter.hasNext(); ) {
                getBuffer().append(iter.next().toString());
                if (iter.hasNext()) {
                    getBuffer().append(", ");
                }
            }
        }
    }

}
