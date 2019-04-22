package com.sonsure.dumper.core.command.entity;

/**
 * Created by liyd on 17/4/14.
 */
public interface Delete extends ConditionCommandExecutor<Delete> {

    /**
     * 指定表
     *
     * @param cls
     * @return
     */
    Delete from(Class<?> cls);

    /**
     * 执行
     *
     * @return
     */
    int execute();
}
