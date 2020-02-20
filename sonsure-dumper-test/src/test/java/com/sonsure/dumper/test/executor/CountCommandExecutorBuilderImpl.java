package com.sonsure.dumper.test.executor;

import com.sonsure.dumper.core.command.CommandContextBuilder;
import com.sonsure.dumper.core.command.CommandExecutor;
import com.sonsure.dumper.core.config.AbstractCommandExecutorBuilder;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

public class CountCommandExecutorBuilderImpl extends AbstractCommandExecutorBuilder {

    @Override
    public boolean support(Class<? extends CommandExecutor> commandExecutorClass, Object param, JdbcEngineConfig jdbcEngineConfig) {
        return commandExecutorClass == CountCommandExecutor.class;
    }

    @Override
    public CommandExecutor build(Class<? extends CommandExecutor> commandExecutorClass, Object param, JdbcEngineConfig jdbcEngineConfig) {
        CountCommandExecutorImpl commandExecutor = new CountCommandExecutorImpl(jdbcEngineConfig);
        CommandContextBuilder commandContextBuilder = new CountCommandContextBuilder();
        commandExecutor.setCommandContextBuilder(commandContextBuilder);
        return commandExecutor;
    }
}
