package com.sonsure.dumper.core.config;


import com.sonsure.dumper.core.command.CommandExecutor;

/**
 * Created by liyd on 17/4/12.
 */
public interface JdbcEngine {

    /**
     * 名称
     *
     * @return
     */
    String getName();

    /**
     * 是否默认
     *
     * @return
     */
    boolean isDefault();

    /**
     * jdbc 配置
     *
     * @return
     */
    JdbcEngineConfig getJdbcEngineConfig();


    /**
     * 创建执行器
     *
     * @param commandExecutorClass 执行器class
     * @param <T>
     * @return
     */
    <T extends CommandExecutor> T createExecutor(Class<T> commandExecutorClass);

}
