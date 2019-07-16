package com.sonsure.dumper.core.command.sql;

import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.mapping.MappingHandler;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;

import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ConcurrentHashMap;

public class JSqlParserCommandConversionHandler implements CommandConversionHandler {

    protected Map<String, String> CACHE = new WeakHashMap<>(new ConcurrentHashMap<String, String>());

    /**
     * 映射处理器
     */
    protected MappingHandler mappingHandler;


    public JSqlParserCommandConversionHandler() {

    }

    public JSqlParserCommandConversionHandler(MappingHandler mappingHandler) {
        this.mappingHandler = mappingHandler;
    }


    @Override
    public String convert(String command, Map<String, Object> params) {

        String convertedCommand = CACHE.get(command);
        if (convertedCommand == null) {
            try {
                Statement statement = CCJSqlParserUtil.parse(command);
                CommandMappingHandler commandMappingHandler = new CommandMappingHandler(statement, mappingHandler, params);
                StringBuilder buffer = new StringBuilder();
                ExpressionDeParser expressionDeParser = new CommandExpressionDeParser();
                SelectDeParser selectDeParser = new CommandSelectDeParser(expressionDeParser, buffer, commandMappingHandler);
                expressionDeParser.setSelectVisitor(selectDeParser);
                expressionDeParser.setBuffer(buffer);
                statement.accept(new CommandStatementDeParser(expressionDeParser, selectDeParser, buffer, commandMappingHandler));
                convertedCommand = buffer.toString();
            } catch (Exception e) {
                throw new SonsureJdbcException("Parsing sql failed:" + command, e);
            }
        }
        return convertedCommand;
    }


    public MappingHandler getMappingHandler() {
        return mappingHandler;
    }

    public void setMappingHandler(MappingHandler mappingHandler) {
        this.mappingHandler = mappingHandler;
    }
}
