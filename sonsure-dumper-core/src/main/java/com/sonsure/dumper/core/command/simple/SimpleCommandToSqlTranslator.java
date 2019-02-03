package com.sonsure.dumper.core.command.simple;

import com.sonsure.dumper.core.command.sql.*;
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.mapping.MappingHandler;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

import java.util.Map;

public class SimpleCommandToSqlTranslator implements CommandToSqlTranslator {

    @Override
    public String getSql(String command, MappingHandler mappingHandler, Map<String, String> classMapping, Map<String, String> customClassMapping) {
        try {
            Statement statement = CCJSqlParserUtil.parse(command);
            StringBuilder buffer = new StringBuilder();
            ExpressionDeParser expressionDeParser = new CommandExpressionDeParser();
            CommandTableHandler commandTableHandler = new CommandTableHandler(mappingHandler, classMapping, customClassMapping);
            SelectDeParser selectDeParser = new CommandSelectDeParser(expressionDeParser, buffer, commandTableHandler);
            expressionDeParser.setSelectVisitor(selectDeParser);
            expressionDeParser.setBuffer(buffer);
            statement.accept(new CommandStatementDeParser(expressionDeParser, selectDeParser, buffer, commandTableHandler));
            return buffer.toString();
        } catch (JSQLParserException e) {
            throw new SonsureJdbcException("解析sql失败:" + command, e);
        }
    }

//    private String parseSelect(Select select, MappingHandler mappingHandler, Map<String, String> classMapping, Map<String, String> customClassMapping) {
//
//        StringBuilder buffer = new StringBuilder();
//        ExpressionDeParser expressionDeParser = new CommandExpressionDeParser();
//        CommandTableHandler commandTableHandler = new CommandTableHandler(mappingHandler, classMapping, customClassMapping);
//        SelectDeParser selectDeParser = new CommandSelectDeParser(expressionDeParser, buffer, commandTableHandler);
//        expressionDeParser.setSelectVisitor(selectDeParser);
//        expressionDeParser.setBuffer(buffer);
//
//
//        select.accept(new CommandStatementDeParser(expressionDeParser, selectDeParser, buffer, commandTableHandler));
////        select.getSelectBody().accept(selectDeParser);
//        return buffer.toString();
//    }
//
//    private String parseUpdate(Update update, MappingHandler mappingHandler, Map<String, String> classMapping, Map<String, String> customClassMapping) {
//
//        StringBuilder buffer = new StringBuilder();
//        ExpressionDeParser expressionDeParser = new CommandExpressionDeParser();
//        CommandTableHandler commandTableHandler = new CommandTableHandler(mappingHandler, classMapping, customClassMapping);
//        SelectDeParser selectDeParser = new CommandSelectDeParser(expressionDeParser, buffer, commandTableHandler);
//        expressionDeParser.setSelectVisitor(selectDeParser);
//        expressionDeParser.setBuffer(buffer);
//        update.accept(new CommandStatementDeParser(expressionDeParser, selectDeParser, buffer, commandTableHandler));
//
//        return buffer.toString();
//    }
//
//    private String parseDelete(Delete delete, MappingHandler mappingHandler, Map<String, String> classMapping, Map<String, String> customClassMapping) {
//        StringBuilder buffer = new StringBuilder();
//        ExpressionDeParser expressionDeParser = new CommandExpressionDeParser();
//        CommandTableHandler commandTableHandler = new CommandTableHandler(mappingHandler, classMapping, customClassMapping);
//        SelectDeParser selectDeParser = new CommandSelectDeParser(expressionDeParser, buffer, commandTableHandler);
//        expressionDeParser.setSelectVisitor(selectDeParser);
//        expressionDeParser.setBuffer(buffer);
//        delete.accept(new CommandStatementDeParser(expressionDeParser, selectDeParser, buffer, commandTableHandler));
//
//        return buffer.toString();
//    }

}
