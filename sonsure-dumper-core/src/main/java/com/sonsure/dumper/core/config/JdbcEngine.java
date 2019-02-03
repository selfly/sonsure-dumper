package com.sonsure.dumper.core.config;


import com.sonsure.dumper.core.command.CommandExecutor;

/**
 * Created by liyd on 17/4/12.
 */
public interface JdbcEngine {


    /**
     * 创建执行器
     *
     * @param commandExecutorClass 执行器class
     * @param <T>
     * @return
     */
    <T extends CommandExecutor> T createExecutor(Class<T> commandExecutorClass);

    /**
     * 创建执行器
     *
     * @param <T>  the type parameter
     * @param modelClass the model class
     * @param commandExecutorClass 执行器class
     * @return t
     */
    <T extends CommandExecutor> T createExecutor(Class<?> modelClass, Class<T> commandExecutorClass);

}
