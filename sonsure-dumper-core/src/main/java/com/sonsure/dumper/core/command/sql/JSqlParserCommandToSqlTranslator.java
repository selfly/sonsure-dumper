package com.sonsure.dumper.core.command.sql;

import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.mapping.MappingHandler;
import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

import java.util.Map;

public class JSqlParserCommandToSqlTranslator implements CommandToSqlTranslator {

    @Override
    public String getSql(String command, MappingHandler mappingHandler, Map<String, Class<?>> classMapping, Map<String, Class<?>> customClassMapping) {
        try {
            Statement statement = CCJSqlParserUtil.parse(command);
            CommandMappingHandler commandMappingHandler = new CommandMappingHandler(statement, mappingHandler, classMapping, customClassMapping);
            StringBuilder buffer = new StringBuilder();
            ExpressionDeParser expressionDeParser = new CommandExpressionDeParser();
            SelectDeParser selectDeParser = new CommandSelectDeParser(expressionDeParser, buffer, commandMappingHandler);
            expressionDeParser.setSelectVisitor(selectDeParser);
            expressionDeParser.setBuffer(buffer);
            statement.accept(new CommandStatementDeParser(expressionDeParser, selectDeParser, buffer, commandMappingHandler));
            return buffer.toString();
        } catch (JSQLParserException e) {
            throw new SonsureJdbcException("解析sql失败:" + command, e);
        }
    }
}
