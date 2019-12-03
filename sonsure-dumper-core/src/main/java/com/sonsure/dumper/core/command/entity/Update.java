package com.sonsure.dumper.core.command.entity;

import com.sonsure.dumper.core.command.lambda.Consumer;

/**
 * Created by liyd on 17/4/14.
 */
public interface Update extends ConditionCommandExecutor<Update> {

    /**
     * 指定表
     *
     * @param cls
     * @return
     */
    Update table(Class<?> cls);

    /**
     * 设置属性值
     *
     * @param field
     * @param value
     */
    Update set(String field, Object value);

    /**
     * 设置属性值
     *
     * @param consumer
     * @param value
     * @param <E>
     * @return
     */
    <E> Update set(Consumer<E> consumer, Object value);

    /**
     * 根据实体类设置属性,主键设置成where条件
     *
     * @param entity
     * @return
     */
    Update setForEntityWhereId(Object entity);

    /**
     * 根据实体类设置属性
     *
     * @param entity
     * @return
     */
    Update setForEntity(Object entity);

    /**
     * 更新null值
     *
     * @return
     */
    Update updateNull();

    /**
     * 执行
     *
     * @return
     */
    int execute();
}
