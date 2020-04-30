/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.persist;


import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;
import com.sonsure.dumper.core.command.CommandExecutor;
import com.sonsure.dumper.core.command.entity.Delete;
import com.sonsure.dumper.core.command.entity.Insert;
import com.sonsure.dumper.core.command.entity.Select;
import com.sonsure.dumper.core.command.entity.Update;
import com.sonsure.dumper.core.command.lambda.Function;
import com.sonsure.dumper.core.command.mybatis.MybatisExecutor;
import com.sonsure.dumper.core.command.natives.NativeExecutor;

import java.io.Serializable;
import java.util.List;

/**
 * Created by liyd on 17/4/12.
 */
public interface JdbcDao {

    /**
     * 指定使用的持久化jdbc对象
     *
     * @param name
     * @return
     */
    JdbcDao use(String name);

    /**
     * 获取一个实体对象
     *
     * @param entityClass
     * @param id
     * @param <T>
     * @return
     */
    <T> T get(Class<T> entityClass, Serializable id);

    /**
     * 查询所有列表
     *
     * @param entityClass
     * @param <T>
     * @return
     */
    <T> List<T> find(Class<T> entityClass);

    /**
     * 根据实体条件查询列表,根据默认主键desc排序
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> List<T> find(T entity);

    /**
     * 查询分页列表
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T extends Pageable> Page<T> pageResult(T entity);

    /**
     * 根据实体条件查询记录数
     *
     * @param entity
     * @return
     */
    long findCount(Object entity);

    /**
     * 根据实体条件查询记录数
     *
     * @param cls the cls
     * @return long
     */
    long findCount(Class<?> cls);

    /**
     * 根据实体条件查询单个结果
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> T singleResult(T entity);

    /**
     * 根据实体条件查询首个结果
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> T firstResult(T entity);

    /**
     * 插入
     *
     * @param entity
     * @param <T>
     * @return
     */
    <T> Object executeInsert(T entity);

    /**
     * 创建insert对象
     *
     * @param cls
     * @return
     */
    Insert insertInto(Class<?> cls);

    /**
     * 删除
     *
     * @param entityClass the entity class
     * @param id          the id
     * @return int
     */
    int executeDelete(Class<?> entityClass, Serializable id);

    /**
     * 根据实体条件删除
     *
     * @param entity the entity
     * @return int int
     */
    int executeDelete(Object entity);

    /**
     * 删除所有对象
     *
     * @param cls the cls
     * @return int int
     */
    int executeDelete(Class<?> cls);

    /**
     * 更新
     *
     * @param entity the entity
     * @return int
     */
    int executeUpdate(Object entity);

    /**
     * 创建update对象
     *
     * @param cls
     * @return
     */
    Update update(Class<?> cls);

    /**
     * 创建select对象
     *
     * @param cls
     * @return
     */
    Select selectFrom(Class<?> cls);

    /**
     * 创建select对象
     *
     * @return select select
     */
    Select select();

    /**
     * 创建select对象
     *
     * @param fields the fields
     * @return select select
     */
    Select select(String... fields);

    /**
     * 创建select对象
     *
     * @param function
     * @return
     */
    <E, R> Select select(Function<E, R> function);

    /**
     * 创建insert对象
     *
     * @return insert insert
     */
    Insert insert();

    /**
     * 创建delete对象
     *
     * @return delete
     */
    Delete delete();

    /**
     * 创建delete对象
     *
     * @param cls the cls
     * @return delete delete
     */
    Delete deleteFrom(Class<?> cls);

    /**
     * 创建update对象
     *
     * @return update update
     */
    Update update();

    /**
     * 执行脚本
     *
     * @param script
     */
    void executeScript(String script);

    /**
     * 创建native executor对象
     *
     * @return
     */
    NativeExecutor nativeExecutor();

    /**
     * 创建mybatis executor对象
     *
     * @return
     */
    MybatisExecutor myBatisExecutor();

    /**
     * 创建自定义executor
     *
     * @param executor
     * @param <T>
     * @return
     */
    <T extends CommandExecutor> T executor(Class<T> executor);
}
