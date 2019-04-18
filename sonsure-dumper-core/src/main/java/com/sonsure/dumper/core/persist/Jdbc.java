package com.sonsure.dumper.core.persist;

import com.sonsure.dumper.core.command.entity.Select;
import com.sonsure.dumper.core.command.natives.NativeExecutor;
import com.sonsure.dumper.core.config.JdbcEngine;
import com.sonsure.dumper.core.exception.SonsureJdbcException;

import java.util.HashMap;
import java.util.Map;

public class Jdbc {

    protected static Map<String, JdbcEngine> jdbcEngineMap = new HashMap<>();

    protected static ThreadLocal<JdbcEngine> jdbcEngineThreadLocal = new ThreadLocal<>();

    protected static JdbcEngine defaultJdbcEngine;

    public static void use(String name) {
        JdbcEngine jdbcEngine = jdbcEngineMap.get(name);
        if (jdbcEngine == null) {
            throw new SonsureJdbcException("指定的JdbcEngine不存在:" + name);
        }
        jdbcEngineThreadLocal.set(jdbcEngine);
    }

    public static JdbcEngine getJdbcEngine() {
        JdbcEngine jdbcEngine = jdbcEngineThreadLocal.get();
        if (jdbcEngine != null) {
            return jdbcEngine;
        }
        if (defaultJdbcEngine == null) {
            throw new SonsureJdbcException("没有指定使用的JdbcEngine，且没有配置默认的JdbcEngine");
        }
        return defaultJdbcEngine;
    }

    public static void addJdbcEngine(JdbcEngine jdbcEngine) {
        defaultJdbcEngine = jdbcEngine;
    }

    public static Select select() {
        return getJdbcEngine().createExecutor(Select.class);
    }

    public static NativeExecutor nativeExecutor() {
        return getJdbcEngine().createExecutor(NativeExecutor.class);
    }


}
