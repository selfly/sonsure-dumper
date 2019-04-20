package com.sonsure.dumper.core.persist;

import com.sonsure.dumper.core.command.entity.Select;
import com.sonsure.dumper.core.command.natives.NativeExecutor;
import com.sonsure.dumper.core.exception.SonsureJdbcException;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Jdbc {

    protected static Map<String, JdbcEngineFacade> jdbcFacadeMap = new HashMap<>();

    protected static JdbcEngineFacade defaultJdbcEngineFacade;

    public static JdbcEngineFacade use(String name) {
        JdbcEngineFacade jdbcFacade = jdbcFacadeMap.get(name);
        if (jdbcFacade == null) {
            throw new SonsureJdbcException("指定的JDBC配置名称不存在:" + name);
        }
        return jdbcFacade;
    }

    public static void addJdbcFacade(JdbcEngineFacade jdbcEngineFacade) {
        jdbcFacadeMap.put(jdbcEngineFacade.getName(), jdbcEngineFacade);
        if (jdbcEngineFacade.isDefault()) {
            if (defaultJdbcEngineFacade != null && defaultJdbcEngineFacade != jdbcEngineFacade) {
                throw new SonsureJdbcException("只能设置一个默认的JDBC配置");
            }
            defaultJdbcEngineFacade = jdbcEngineFacade;
        }
    }

    public static JdbcEngineFacade getDefaultJdbcEngineFacade() {
        if (defaultJdbcEngineFacade == null) {
            throw new SonsureJdbcException("没有默认的Jdbc配置");
        }
        return defaultJdbcEngineFacade;
    }


    public static Select select() {
        return getDefaultJdbcEngineFacade().select();
    }

    public static Select selectFrom(Class<?> cls) {
        return select().from(cls);
    }

    public static <T> T get(Class<T> cls, Serializable id) {
        return getDefaultJdbcEngineFacade().get(cls, id);
    }

    public static Object insert(Object entity) {
        return getDefaultJdbcEngineFacade().insert().forEntity(entity).execute();
    }

    public static int update(Object entity) {
        return getDefaultJdbcEngineFacade().update().setForEntityWhereId(entity).execute();
    }

    public static int delete(Class<?> cls, Serializable id) {
        String pkField = getDefaultJdbcEngineFacade().getJdbcEngine().getJdbcEngineConfig().getMappingHandler().getPkField(cls);
        return getDefaultJdbcEngineFacade().delete().from(cls).where(pkField, id).execute();
    }

    public static NativeExecutor nativeExecutor() {
        return null;
    }


}
