package com.sonsure.dumper.core.command.sql;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Pivot;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

public class CommandSelectDeParser extends SelectDeParser {

    private CommandTableHandler commandTableHandler;

    public CommandSelectDeParser(ExpressionVisitor expressionVisitor, StringBuilder buffer, CommandTableHandler commandTableHandler) {
        super(expressionVisitor, buffer);
        this.commandTableHandler = commandTableHandler;
    }

    @Override
    public void visit(PlainSelect plainSelect) {
        this.commandTableHandler.extractTable(plainSelect);
        super.visit(plainSelect);
    }

    @Override
    public void visit(Table table) {

        String tbName = this.commandTableHandler.getTableName(table.getName());

        table.setName(tbName);

        StringBuilder buffer = getBuffer();
        buffer.append(table.getFullyQualifiedName());
        Pivot pivot = table.getPivot();
        if (pivot != null) {
            pivot.accept(this);
        }
        Alias alias = table.getAlias();
        if (alias != null) {
            buffer.append(alias);
        }
    }

    public CommandTableHandler getCommandTableHandler() {
        return commandTableHandler;
    }
}
