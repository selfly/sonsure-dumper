package com.sonsure.dumper.core.command.mybatis;

import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.simple.AbstractSimpleCommandContextBuilder;
import com.sonsure.dumper.core.command.sql.CommandResolver;
import com.sonsure.dumper.core.management.CommandTable;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MybatisCommandContextBuilder extends AbstractSimpleCommandContextBuilder {

    private SqlSessionFactory sqlSessionFactory;

    public MybatisCommandContextBuilder(AbstractCommandExecutor commandExecutor, CommandResolver commandResolver, SqlSessionFactory sqlSessionFactory) {
        super(commandExecutor, commandResolver);
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public CommandContext doBuild(CommandTable commandTable) {

        String command = (String) commandTable.getExtendData(CommandTable.ExtendDataKey.COMMAND.name());
        Map<String, Object> parameters = (Map<String, Object>) commandTable.getExtendData(CommandTable.ExtendDataKey.PARAMETERS.name());

        MappedStatement statement = sqlSessionFactory.getConfiguration().getMappedStatement(command);
        Configuration configuration = sqlSessionFactory.getConfiguration();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        BoundSql boundSql = statement.getBoundSql(parameters);
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        List<Object> params = new ArrayList<>();
        if (parameterMappings != null) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) { // issue #448 ask first for additional params
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    params.add(value);
                }
            }
        }
        String sql = this.resolveCommand(boundSql.getSql(), commandTable, commandExecutor.getMappingHandler());
        CommandContext commandContext = new CommandContext();
        commandContext.setCommand(sql);
        if (parameters != null) {
            commandContext.addParameters(params);
        }
        return commandContext;
    }
}
