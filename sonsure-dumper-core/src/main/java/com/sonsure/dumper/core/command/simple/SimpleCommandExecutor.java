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
     * 是否强制native，command不做任何加工
     *
     * @param nativeSql the is force native
     * @return t
     */
    T nativeSql(boolean nativeSql);

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
     * @param pkColumn
     * @return
     */
    Serializable insert(String pkColumn);

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
