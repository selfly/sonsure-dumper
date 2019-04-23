package com.sonsure.dumper.core.persist;

import com.sonsure.commons.model.Page;
import com.sonsure.commons.model.Pageable;
import com.sonsure.commons.utils.UUIDUtils;
import com.sonsure.dumper.core.command.entity.Insert;
import com.sonsure.dumper.core.command.entity.Select;
import com.sonsure.dumper.core.command.natives.NativeExecutor;
import com.sonsure.dumper.core.config.JdbcEngine;
import com.sonsure.dumper.core.exception.SonsureJdbcException;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Jdbc {

    protected final static Map<String, JdbcEngine> jdbcEngineMap = new HashMap<>();

    protected static JdbcEngine defaultJdbcEngine;

    public static JdbcEngine use(String name) {
        JdbcEngine jdbcEngine = jdbcEngineMap.get(name);
        if (jdbcEngine == null) {
            throw new SonsureJdbcException("指定的JDBC配置名称不存在:" + name);
        }
        return jdbcEngine;
    }

    public static void addJdbcEngine(JdbcEngine jdbcEngine) {
        String name = jdbcEngine.getName();
        if (StringUtils.isBlank(name)) {
            //没有指定随机生成一个
            name = UUIDUtils.getUUID8();
        }
        jdbcEngineMap.put(name, jdbcEngine);
        if (jdbcEngine.isDefault()) {
            if (defaultJdbcEngine != null && defaultJdbcEngine != jdbcEngine) {
                throw new SonsureJdbcException("只能设置一个默认的JDBC配置");
            }
            defaultJdbcEngine = jdbcEngine;
        }
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

    public static Select selectFrom(Class<?> cls) {
        return select().from(cls);
    }

    public static <T> T get(Class<T> cls, Serializable id) {
        return getDefaultJdbcEngine().get(cls, id);
    }

    public static Object insert(Object entity) {
        return getDefaultJdbcEngine().insert().forEntity(entity).execute();
    }

    public static Insert insertInto(Class<?> cls) {
        return getDefaultJdbcEngine().insertInto(cls);
    }

    public static int update(Object entity) {
        return getDefaultJdbcEngine().update().setForEntityWhereId(entity).execute();
    }

    public static int delete(Class<?> cls, Serializable id) {
        String pkField = getDefaultJdbcEngine().getJdbcEngineConfig().getMappingHandler().getPkField(cls);
        return getDefaultJdbcEngine().delete().from(cls).where(pkField, id).execute();
    }

    public static int executeDelete(Object entity) {
        return getDefaultJdbcEngine().executeDelete(entity);
    }

    public static int executeDelete(Class<?> cls) {
        return getDefaultJdbcEngine().executeDelete(cls);
    }

    public static <T> List<T> find(Class<T> cls) {
        String pkField = getDefaultJdbcEngine().getJdbcEngineConfig().getMappingHandler().getPkField(cls);
        return getDefaultJdbcEngine().selectFrom(cls).orderBy(pkField).desc().list(cls);
    }

    public static <T> List<T> find(T entity) {
        String pkField = getDefaultJdbcEngine().getJdbcEngineConfig().getMappingHandler().getPkField(entity.getClass());
        return (List<T>) getDefaultJdbcEngine().selectFrom(entity.getClass()).where().conditionEntity(entity).orderBy(pkField).desc().list(entity.getClass());
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
        return null;
    }


}
