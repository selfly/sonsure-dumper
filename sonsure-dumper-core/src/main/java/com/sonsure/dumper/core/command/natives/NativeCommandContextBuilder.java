package com.sonsure.dumper.core.command.natives;

import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.ExecutorContext;
import com.sonsure.dumper.core.command.entity.AbstractCommandContextBuilder;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

/**
 * @author liyd
 */
public class NativeCommandContextBuilder extends AbstractCommandContextBuilder {

    @Override
    public CommandContext doBuild(ExecutorContext executorContext, JdbcEngineConfig jdbcEngineConfig) {
        NativeExecutorContext nativeExecutorContext = (NativeExecutorContext) executorContext;
        CommandContext commandContext = new CommandContext();
        commandContext.setCommand(nativeExecutorContext.getCommand());
        if (nativeExecutorContext.getParameters() != null) {
            commandContext.addParameters(nativeExecutorContext.getParameters());
        }
        return commandContext;
    }
}
