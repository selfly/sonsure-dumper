package com.sonsure.dumper.core.command;


import com.sonsure.dumper.core.config.JdbcEngineConfig;

/**
 * Created by liyd on 17/4/12.
 */
public interface CommandContextBuilderFactory {


    /**
     * 支持的类型
     *
     * @return
     */
    Class<?>[] getSupportType();


    /**
     * 实现构建的命令类型
     *
     * @return
     */
    Class<? extends CommandExecutor> getExecutorInterface();

    /**
     * 获取构建器
     *
     * @param clazz            类型，为上面getSupportType返回的其中一个
     * @param jdbcEngineConfig the jdbc engine config
     * @return command context builder
     */
    CommandContextBuilder getCommandContextBuilder(Class<?> clazz, JdbcEngineConfig jdbcEngineConfig);

}
