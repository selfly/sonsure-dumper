package com.sonsure.dumper.core.command.mybatis;

import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.ExecutorContext;
import com.sonsure.dumper.core.command.entity.AbstractCommandContextBuilder;
import com.sonsure.dumper.core.command.sql.CommandConversionHandler;
import org.apache.ibatis.mapping.BoundSql;
import org.apache.ibatis.mapping.MappedStatement;
import org.apache.ibatis.mapping.ParameterMapping;
import org.apache.ibatis.mapping.ParameterMode;
import org.apache.ibatis.reflection.MetaObject;
import org.apache.ibatis.session.Configuration;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.type.TypeHandlerRegistry;

import java.util.List;
import java.util.Map;

public class MybatisCommandContextBuilder extends AbstractCommandContextBuilder {

    private SqlSessionFactory sqlSessionFactory;

    public MybatisCommandContextBuilder(AbstractCommandExecutor commandExecutor, CommandConversionHandler commandConversionHandler, SqlSessionFactory sqlSessionFactory) {
        super(commandExecutor, commandConversionHandler);
        this.sqlSessionFactory = sqlSessionFactory;
    }

    @Override
    public CommandContext doBuild(ExecutorContext commandTable) {

        CommandContext commandContext = new CommandContext();

        String command = (String) commandTable.getExtendData(ExecutorContext.ExtendDataKey.COMMAND.name());
        Map<String, Object> parameters = (Map<String, Object>) commandTable.getExtendData(ExecutorContext.ExtendDataKey.PARAMETERS.name());

        MappedStatement statement = sqlSessionFactory.getConfiguration().getMappedStatement(command);
        Configuration configuration = sqlSessionFactory.getConfiguration();
        TypeHandlerRegistry typeHandlerRegistry = configuration.getTypeHandlerRegistry();
        BoundSql boundSql = statement.getBoundSql(parameters);
        Object parameterObject = boundSql.getParameterObject();
        List<ParameterMapping> parameterMappings = boundSql.getParameterMappings();
        if (parameterMappings != null) {
            for (int i = 0; i < parameterMappings.size(); i++) {
                ParameterMapping parameterMapping = parameterMappings.get(i);
                if (parameterMapping.getMode() != ParameterMode.OUT) {
                    Object value;
                    String propertyName = parameterMapping.getProperty();
                    if (boundSql.hasAdditionalParameter(propertyName)) {
                        value = boundSql.getAdditionalParameter(propertyName);
                    } else if (parameterObject == null) {
                        value = null;
                    } else if (typeHandlerRegistry.hasTypeHandler(parameterObject.getClass())) {
                        value = parameterObject;
                    } else {
                        MetaObject metaObject = configuration.newMetaObject(parameterObject);
                        value = metaObject.getValue(propertyName);
                    }
                    commandContext.addParameter(propertyName, value);
                }
            }
        }

        commandContext.setCommand(boundSql.getSql());
        return commandContext;
    }
}
