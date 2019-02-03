package com.sonsure.dumper.core.command.sql;

import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.update.Update;
import net.sf.jsqlparser.util.deparser.*;

public class CommandStatementDeParser extends StatementDeParser {

    protected ExpressionDeParser expressionDeParser;
    protected SelectDeParser selectDeParser;
    protected CommandTableHandler commandTableHandler;

    public CommandStatementDeParser(ExpressionDeParser expressionDeParser, SelectDeParser selectDeParser, StringBuilder buffer, CommandTableHandler commandTableHandler) {
        super(expressionDeParser, selectDeParser, buffer);
        this.expressionDeParser = expressionDeParser;
        this.selectDeParser = selectDeParser;
        this.commandTableHandler = commandTableHandler;
    }

    @Override
    public void visit(Update update) {
        commandTableHandler.extractTable(update);
        selectDeParser.setBuffer(getBuffer());
        expressionDeParser.setSelectVisitor(selectDeParser);
        expressionDeParser.setBuffer(getBuffer());
        UpdateDeParser updateDeParser = new CommandUpdateDeParser(expressionDeParser, selectDeParser, getBuffer(), commandTableHandler);
        selectDeParser.setExpressionVisitor(expressionDeParser);
        updateDeParser.deParse(update);
    }

    @Override
    public void visit(Delete delete) {
        commandTableHandler.extractTable(delete);
        selectDeParser.setBuffer(getBuffer());
        expressionDeParser.setSelectVisitor(selectDeParser);
        expressionDeParser.setBuffer(getBuffer());
        selectDeParser.setExpressionVisitor(expressionDeParser);
        DeleteDeParser deleteDeParser = new CommandDeleteDeParser(expressionDeParser, getBuffer(), this.commandTableHandler);
        deleteDeParser.deParse(delete);
    }

    @Override
    public void visit(Insert insert) {
        commandTableHandler.extractTable(insert);
        selectDeParser.setBuffer(getBuffer());
        expressionDeParser.setSelectVisitor(selectDeParser);
        expressionDeParser.setBuffer(getBuffer());
        selectDeParser.setExpressionVisitor(expressionDeParser);
        InsertDeParser insertDeParser = new CommandInsertDeParser(expressionDeParser, selectDeParser, getBuffer(), this.commandTableHandler);
        insertDeParser.deParse(insert);
    }

    public CommandTableHandler getCommandTableHandler() {
        return commandTableHandler;
    }
}
