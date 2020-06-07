/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.natives;

import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandExecutorContext;
import com.sonsure.dumper.core.command.entity.AbstractCommandContextBuilder;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

/**
 * @author liyd
 */
public class NativeCommandContextBuilder extends AbstractCommandContextBuilder {

    @Override
    public CommandContext doBuild(CommandExecutorContext executorContext, JdbcEngineConfig jdbcEngineConfig) {
        CommandContext commandContext = new CommandContext();
        commandContext.setCommand(executorContext.getCommand());
        if (executorContext.getCommandParameters() != null) {
            commandContext.addCommandParameters(executorContext.getCommandParameters());
        }
        return commandContext;
    }
}
