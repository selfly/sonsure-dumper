package com.sonsure.dumper.core.command.entity;


import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.ExecutorContext;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/14.
 */
public class DeleteCommandContextBuilderImpl extends AbstractCommandContextBuilder {

    private static final String COMMAND_OPEN = "delete from ";

    public CommandContext doBuild(ExecutorContext executorContext, JdbcEngineConfig jdbcEngineConfig) {
//        StringBuilder command = new StringBuilder(COMMAND_OPEN);
//        command.append(this.getModelAliasName(commandTable.getModelClass(), commandTable.getTableAlias()));
//
//        CommandContext whereCommandContext = this.buildWhereSql(commandTable);
//        command.append(whereCommandContext.getCommand());
//        CommandContext commandContext = getCommonCommandContext(commandTable);
//        commandContext.setCommand(command.toString());
//        commandContext.addParameters(whereCommandContext.getParameterMap());

        return null;
    }
}
