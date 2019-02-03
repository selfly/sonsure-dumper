package com.sonsure.dumper.core.config;

import com.sonsure.dumper.core.command.CommandExecutor;

public interface CommandExecutorBuilder {

    /**
     * 是否支持
     *
     * @param modelClass
     * @param commandExecutorClass
     * @param jdbcEngineConfig
     * @return
     */
    boolean support(Class<?> modelClass, Class<?> commandExecutorClass, JdbcEngineConfig jdbcEngineConfig);

    /**
     * 构建CommandExecutor
     *
     * @param modelClass
     * @param commandExecutorClass
     * @param jdbcEngineConfig
     * @return
     */
    CommandExecutor build(Class<?> modelClass, Class<?> commandExecutorClass, JdbcEngineConfig jdbcEngineConfig);
}
