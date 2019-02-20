package com.sonsure.dumper.core.command.mybatis;

import com.sonsure.dumper.core.command.simple.SimpleCommandExecutor;

import java.util.Map;

public interface MybatisExecutor extends SimpleCommandExecutor<MybatisExecutor> {

    /**
     * 参数
     *
     * @param parameters the parameters
     * @return mybatis executor
     */
    MybatisExecutor parameters(Map<String, Object> parameters);





}
