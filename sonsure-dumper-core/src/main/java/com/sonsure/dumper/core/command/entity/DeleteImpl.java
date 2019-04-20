package com.sonsure.dumper.core.command.entity;


import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/14.
 */
public class DeleteImpl<T> extends AbstractConditionBuilder<Delete<T>> implements Delete<T> {

    public DeleteImpl(JdbcEngineConfig jdbcEngineConfig) {
        super(jdbcEngineConfig);
    }

    public int execute() {
        CommandContext commandContext = this.commandContextBuilder.build(null, getJdbcEngineConfig());
        return (Integer) getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.DELETE);
    }

    @Override
    protected WhereContext getWhereContext() {
        return null;
    }
}
