package com.sonsure.dumper.core.command.simple;


import com.sonsure.dumper.core.command.QueryCommandExecutor;

import java.io.Serializable;

/**
 * Created by liyd on 17/4/25.
 */
public interface SimpleCommandExecutor<T extends SimpleCommandExecutor<T>> extends QueryCommandExecutor<T> {

    /**
     * 命令
     *
     * @param command
     * @return
     */
    T command(String command);

    /**
     * 是否禁止转换，command不做任何加工
     *
     * @return t
     */
    T nativeCommand();

    /**
     * 结果处理器
     *
     * @param resultHandler
     * @param <E>
     * @return
     */
    <E> T resultHandler(ResultHandler<E> resultHandler);

    /**
     * 插入
     */
    void insert();

    /**
     * 插入 返回主键
     *
     * @param clazz the clazz
     * @return serializable
     */
    Serializable insert(Class<?> clazz);

    /**
     * 更新
     *
     * @return
     */
    int update();

    /**
     * 执行
     */
    void execute();
}
