package com.sonsure.dumper.core.config;


import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;
import com.sonsure.dumper.core.command.CommandExecutor;
import com.sonsure.dumper.core.command.entity.Delete;
import com.sonsure.dumper.core.command.entity.Insert;
import com.sonsure.dumper.core.command.entity.Select;
import com.sonsure.dumper.core.command.entity.Update;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liyd on 17/4/12.
 */
public interface JdbcEngine {

    /**
     * 名称
     *
     * @return
     */
    String getName();

    /**
     * 是否默认
     *
     * @return
     */
    boolean isDefault();

    /**
     * jdbc 配置
     *
     * @return
     */
    JdbcEngineConfig getJdbcEngineConfig();


    /**
     * 创建执行器
     *
     * @param commandExecutorClass 执行器class
     * @param <T>
     * @return
     */
    <T extends CommandExecutor> T createExecutor(Class<T> commandExecutorClass);

    /**
     * insert对象
     *
     * @return
     */
    Insert insert();

    /**
     * insert数据
     *
     * @param entity
     * @return
     */
    Object insert(Object entity);

    /**
     * 创建insert对象后指定into对象
     *
     * @param cls
     * @return
     */
    Insert insertInto(Class<?> cls);

    /**
     * select对象
     *
     * @return
     */
    Select select();


    /**
     * 查询所有列表
     *
     * @param cls
     * @param <T>
     * @return
     */
    <T> List<T> find(Class<T> cls);

    /**
     * 查询列表，以entity中不为null属性为where条件
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> List<T> find(T entity);

    /**
     * 查询分页列表,以entity中不为null属性为where条件
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T extends Pageable> Page<T> pageResult(T entity);

    /**
     * 查询记录数
     *
     * @param entity
     * @return
     */
    long findCount(Object entity);

    /**
     * 查询记录数
     *
     * @param cls the cls
     * @return long
     */
    long findCount(Class<?> cls);

    /**
     * 查询单个结果
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> T singleResult(T entity);

    /**
     * 第一个结果
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> T firstResult(T entity);


    /**
     * update对象
     *
     * @return
     */
    Update update();

    /**
     * delete对象
     *
     * @return
     */
    Delete delete();

    /**
     * delete对象
     *
     * @param entity
     * @return
     */
    int delete(Object entity);

    /**
     * 删除对象
     *
     * @param cls
     * @return
     */
    int delete(Class<?> cls);

    /**
     * 创建select后指定from
     *
     * @param cls
     * @return
     */
    Select selectFrom(Class<?> cls);


    /**
     * 直接get对象
     *
     * @param cls
     * @param id
     * @param <T>
     * @return
     */
    <T> T get(Class<T> cls, Serializable id);


}
