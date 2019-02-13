package com.sonsure.dumper.core.command.sql;

import net.sf.jsqlparser.expression.Alias;
import net.sf.jsqlparser.expression.ExpressionVisitor;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.select.Pivot;
import net.sf.jsqlparser.statement.select.PlainSelect;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

public class CommandSelectDeParser extends SelectDeParser {

    private CommandMappingHandler commandMappingHandler;

    public CommandSelectDeParser(ExpressionVisitor expressionVisitor, StringBuilder buffer, CommandMappingHandler commandMappingHandler) {
        super(expressionVisitor, buffer);
        this.commandMappingHandler = commandMappingHandler;
    }

    @Override
    public void visit(PlainSelect plainSelect) {
        super.visit(plainSelect);
    }

    @Override
    public void visit(Table table) {

        String tbName = this.commandMappingHandler.getTableName(table);

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

    public CommandMappingHandler getCommandMappingHandler() {
        return commandMappingHandler;
    }
}
