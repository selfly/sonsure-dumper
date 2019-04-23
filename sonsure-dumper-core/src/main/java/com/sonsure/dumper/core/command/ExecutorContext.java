package com.sonsure.dumper.core.command;

/**
 * Created by liyd on 17/4/11.
 */
public interface ExecutorContext {

    /**
     * 获取实体类
     *
     * @return
     */
    Class<?>[] getModelClasses();

    boolean isNativeSql();

}
