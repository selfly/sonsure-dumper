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
import com.sonsure.dumper.core.command.ExecutorContext;
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
    public CommandContext doBuild(ExecutorContext executorContext, JdbcEngineConfig jdbcEngineConfig) {
        DeleteContext deleteContext = (DeleteContext) executorContext;
        StringBuilder command = new StringBuilder(COMMAND_OPEN);
        command.append(this.getModelAliasName(deleteContext.getModelClass(), null));

        CommandContext commandContext = getCommonCommandContext(deleteContext);

        CommandContext whereCommandContext = this.buildWhereSql(deleteContext);
        if (whereCommandContext != null) {
            command.append(whereCommandContext.getCommand());
            commandContext.addParameters(whereCommandContext.getParameters());
        }
        commandContext.setCommand(command.toString());
        return commandContext;
    }
}
