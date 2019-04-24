package com.sonsure.dumper.core.config;

import com.sonsure.dumper.core.command.CommandExecutor;

public interface CommandExecutorBuilder {

    /**
     * 是否支持
     *
     * @param commandExecutorClass the command executor class
     * @param param                the param
     * @param jdbcEngineConfig     the jdbc engine config
     * @return boolean boolean
     */
    boolean support(Class<? extends CommandExecutor> commandExecutorClass, Object param, JdbcEngineConfig jdbcEngineConfig);

    /**
     * 构建CommandExecutor
     *
     * @param commandExecutorClass the command executor class
     * @param param                the param
     * @param jdbcEngineConfig     the jdbc engine config
     * @return command executor
     */
    CommandExecutor build(Class<? extends CommandExecutor> commandExecutorClass, Object param, JdbcEngineConfig jdbcEngineConfig);
}
