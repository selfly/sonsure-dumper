package com.sonsure.dumper.core.config;

import com.sonsure.dumper.core.command.CommandExecutor;

public interface CommandExecutorFactory {


    /**
     * 获取commandExecutor
     *
     * @param modelClass           the model class
     * @param commandExecutorClass the command executor class
     * @param jdbcEngineConfig     the jdbc engine config
     * @return command executor
     */
    CommandExecutor getCommandExecutor(Class<?> modelClass, Class<?> commandExecutorClass, JdbcEngineConfig jdbcEngineConfig);

}
