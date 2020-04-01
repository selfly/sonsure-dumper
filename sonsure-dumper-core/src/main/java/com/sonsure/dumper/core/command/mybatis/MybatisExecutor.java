package com.sonsure.dumper.core.command.mybatis;

import com.sonsure.dumper.core.command.simple.SimpleCommandExecutor;

import java.util.Map;

/**
 * @author liyd
 */
public interface MybatisExecutor extends SimpleCommandExecutor<MybatisExecutor> {

    /**
     * 参数
     *
     * @param parameters the parameters
     * @return mybatis executor
     */
    MybatisExecutor parameters(Map<String, Object> parameters);


    /**
     * 参数
     *
     * @param name  the name
     * @param value the value
     * @return mybatis executor
     */
    MybatisExecutor parameter(String name, Object value);


}
