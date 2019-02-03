package com.sonsure.dumper.core.command.entity;

/**
 * Created by liyd on 17/4/14.
 */
public interface Update<T> extends ConditionBuilder<Update<T>> {

    /**
     * 设置属性值
     *
     * @param field
     * @param value
     */
    Update<T> set(String field, Object value);

    /**
     * 根据实体类设置属性,主键设置成where条件
     *
     * @param entity
     * @return
     */
    Update<T> setForEntityWhereId(Object entity);

    /**
     * 根据实体类设置属性
     *
     * @param entity
     * @return
     */
    Update<T> setForEntity(Object entity);

    /**
     * 更新null值
     *
     * @return
     */
    Update<T> updateNull();

    /**
     * 执行
     *
     * @return
     */
    int execute();
}
