package com.sonsure.dumper.core.command.sql;

import com.sonsure.dumper.core.exception.SonsureJdbcException;
import com.sonsure.dumper.core.mapping.MappingHandler;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.util.deparser.ExpressionDeParser;
import net.sf.jsqlparser.util.deparser.SelectDeParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JSqlParserCommandConversionHandler implements CommandConversionHandler {

    protected static final Logger LOG = LoggerFactory.getLogger(CommandConversionHandler.class);

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
    public String convert(String command) {
        try {
            Statement statement = CCJSqlParserUtil.parse(command);
            CommandMappingHandler commandMappingHandler = new CommandMappingHandler(statement, mappingHandler);
            StringBuilder buffer = new StringBuilder();
            ExpressionDeParser expressionDeParser = new CommandExpressionDeParser();
            SelectDeParser selectDeParser = new CommandSelectDeParser(expressionDeParser, buffer, commandMappingHandler);
            expressionDeParser.setSelectVisitor(selectDeParser);
            expressionDeParser.setBuffer(buffer);
            statement.accept(new CommandStatementDeParser(expressionDeParser, selectDeParser, buffer, commandMappingHandler));
            return buffer.toString();
        } catch (Exception e) {
            throw new SonsureJdbcException("解析sql失败:" + command, e);
        }
    }


    public MappingHandler getMappingHandler() {
        return mappingHandler;
    }

    public void setMappingHandler(MappingHandler mappingHandler) {
        this.mappingHandler = mappingHandler;
    }
}
