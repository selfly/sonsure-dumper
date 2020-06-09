/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.entity;


import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandExecutorContext;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

/**
 * The type Delete command context builder.
 *
 * @author liyd
 * @date 17 /4/14
 */
public class DeleteCommandContextBuilderImpl extends AbstractCommandContextBuilder {

    private static final String COMMAND_OPEN = "delete from ";

    @Override
    public CommandContext doBuild(CommandExecutorContext executorContext, JdbcEngineConfig jdbcEngineConfig) {
        StringBuilder command = new StringBuilder(COMMAND_OPEN);
        final Class<?> modelClass = executorContext.getUniqueModelClass();
        command.append(this.getModelAliasName(modelClass, null));

        CommandContext commandContext = getCommonCommandContext(executorContext);

        CommandContext whereCommandContext = this.buildWhereSql(executorContext);
        if (whereCommandContext != null) {
            command.append(whereCommandContext.getCommand());
            commandContext.addCommandParameters(whereCommandContext.getCommandParameters());
        }
        commandContext.setCommand(command.toString());
        return commandContext;
    }
}
