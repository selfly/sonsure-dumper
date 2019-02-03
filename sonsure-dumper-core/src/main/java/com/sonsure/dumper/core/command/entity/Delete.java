package com.sonsure.dumper.core.command.entity;

/**
 * Created by liyd on 17/4/14.
 */
public interface Delete<T> extends ConditionBuilder<Delete<T>> {

    /**
     * 执行
     *
     * @return
     */
    int execute();
}
