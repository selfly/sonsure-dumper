package com.sonsure.dumper.core.command.entity;

/**
 * Created by liyd on 17/4/14.
 */
public interface Insert extends EntityCommandExecutor {

    /**
     * into对应class
     *
     * @param cls
     * @return
     */
    Insert into(Class<?> cls);

    /**
     * 设置属性值
     *
     * @param field
     * @param value
     */
    Insert set(String field, Object value);

    /**
     * 根据实体类设置属性
     *
     * @param entity
     * @return
     */
    Insert forEntity(Object entity);

    /**
     * 执行
     *
     * @return
     */
    Object execute();
}
