package com.sonsure.dumper.core.command.entity;

/**
 * Created by liyd on 17/4/14.
 */
public interface Insert<T extends Object> extends EntityCommandExecutor {

    /**
     * 设置属性值
     *
     * @param field
     * @param value
     */
    Insert<T> set(String field, Object value);

    /**
     * 根据实体类设置属性
     *
     * @param entity
     * @return
     */
    Insert<T> setForEntity(Object entity);

    /**
     * 执行
     *
     * @return
     */
    Object execute();
}
