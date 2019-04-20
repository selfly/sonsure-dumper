package com.sonsure.dumper.core.command.mybatis;


import com.sonsure.dumper.core.command.simple.AbstractSimpleCommandExecutor;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

import java.util.Map;

/**
 * Created by liyd on 17/4/25.
 */
public class MybatisExecutorImpl extends AbstractSimpleCommandExecutor<MybatisExecutor> implements MybatisExecutor {


    public MybatisExecutorImpl(JdbcEngineConfig jdbcEngineConfig) {
        super(jdbcEngineConfig);
    }

    @Override
    public MybatisExecutor parameters(Map<String, Object> parameters) {
        if (parameters != null) {
//            this.parameters.putAll(parameters);
        }
        return this;
    }

}
