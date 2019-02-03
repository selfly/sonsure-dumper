package com.sonsure.dumper.core.config;


import com.sonsure.dumper.core.command.CommandExecutor;

/**
 * Created by liyd on 17/4/12.
 */
public class JdbcEngineImpl implements JdbcEngine {

    private JdbcEngineConfig jdbcEngineConfig;

    public JdbcEngineImpl(JdbcEngineConfig jdbcEngineConfig) {
        this.jdbcEngineConfig = jdbcEngineConfig;
    }


    public <T extends CommandExecutor> T createExecutor(Class<T> commandExecutorClass) {
        return this.createExecutor(commandExecutorClass, commandExecutorClass);
    }

    @SuppressWarnings("unchecked")
    public <T extends CommandExecutor> T  createExecutor(Class<?> modelClass, Class<T> commandExecutorClass) {
        CommandExecutor commandExecutor = this.jdbcEngineConfig.getCommandExecutor(modelClass, commandExecutorClass);
        return (T) commandExecutor;
    }

}
