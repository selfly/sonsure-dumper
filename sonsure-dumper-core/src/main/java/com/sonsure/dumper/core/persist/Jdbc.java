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
import com.sonsure.dumper.core.command.entity.Delete;
import com.sonsure.dumper.core.command.entity.Insert;
import com.sonsure.dumper.core.command.entity.Select;
import com.sonsure.dumper.core.command.entity.Update;
import com.sonsure.dumper.core.command.mybatis.MybatisExecutor;
import com.sonsure.dumper.core.command.natives.NativeExecutor;
import com.sonsure.dumper.core.config.JdbcEngine;
import com.sonsure.dumper.core.exception.SonsureJdbcException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jdbc {

    protected final static Map<String, JdbcEngine> jdbcEngineMap = new HashMap<>();

    protected static JdbcEngine defaultJdbcEngine;

    public static DaoTemplate use(String name) {
        JdbcEngine jdbcEngine = jdbcEngineMap.get(name);
        if (jdbcEngine == null) {
            throw new SonsureJdbcException("指定的JDBC配置名称不存在:" + name);
        }
        return new FlexibleDaoTemplate(jdbcEngine);
    }

    public static void setDefaultJdbcEngine(JdbcEngine jdbcEngine) {
        defaultJdbcEngine = jdbcEngine;
    }

    public static void addJdbcEngine(Map<String, JdbcEngine> jdbcEngines) {
        if (jdbcEngines == null) {
            return;
        }
        jdbcEngineMap.putAll(jdbcEngines);
    }

    public static JdbcEngine getDefaultJdbcEngine() {
        if (defaultJdbcEngine == null) {
            throw new SonsureJdbcException("没有默认的Jdbc配置");
        }
        return defaultJdbcEngine;
    }


    public static Select select() {
        return getDefaultJdbcEngine().select();
    }

    public static Select select(String... fields) {
        return getDefaultJdbcEngine().select(fields);
    }

    public static Select selectFrom(Class<?> cls) {
        return getDefaultJdbcEngine().selectFrom(cls);
    }

    public static <T> T get(Class<T> cls, Serializable id) {
        return getDefaultJdbcEngine().get(cls, id);
    }

    public static Object executeInsert(Object entity) {
        return getDefaultJdbcEngine().executeInsert(entity);
    }

    public static Insert insertInto(Class<?> cls) {
        return getDefaultJdbcEngine().insertInto(cls);
    }

    public static int executeUpdate(Object entity) {
        return getDefaultJdbcEngine().executeUpdate(entity);
    }

    public static int executeDelete(Class<?> cls, Serializable id) {
        return getDefaultJdbcEngine().executeDelete(cls, id);
    }

    public static Delete delete() {
        return getDefaultJdbcEngine().delete();
    }

    public static int executeDelete(Object entity) {
        return getDefaultJdbcEngine().executeDelete(entity);
    }

    public static int executeDelete(Class<?> cls) {
        return getDefaultJdbcEngine().executeDelete(cls);
    }

    public static Delete deleteFrom(Class<?> cls) {
        return getDefaultJdbcEngine().deleteFrom(cls);
    }

    public static <T> List<T> find(Class<T> cls) {
        return getDefaultJdbcEngine().find(cls);
    }

    public static <T> List<T> find(T entity) {
        return getDefaultJdbcEngine().find(entity);
    }

    public static Insert insert() {
        return getDefaultJdbcEngine().insert();
    }

    public static Update update(Class<?> cls) {
        return getDefaultJdbcEngine().update(cls);
    }

    public static Update update() {
        return getDefaultJdbcEngine().update();
    }

    public static <T extends Pageable> Page<T> pageResult(T entity) {
        return getDefaultJdbcEngine().pageResult(entity);
    }

    public static long findCount(Object entity) {
        return getDefaultJdbcEngine().findCount(entity);
    }

    public static long findCount(Class<?> cls) {
        return getDefaultJdbcEngine().findCount(cls);
    }

    public static <T> T singleResult(T entity) {
        return getDefaultJdbcEngine().singleResult(entity);
    }

    public static <T> T firstResult(T entity) {
        return getDefaultJdbcEngine().firstResult(entity);
    }

    public static NativeExecutor nativeExecutor() {
        return getDefaultJdbcEngine().createExecutor(NativeExecutor.class);
    }

    public static MybatisExecutor myBatisExecutor() {
        return getDefaultJdbcEngine().createExecutor(MybatisExecutor.class);
    }
}
