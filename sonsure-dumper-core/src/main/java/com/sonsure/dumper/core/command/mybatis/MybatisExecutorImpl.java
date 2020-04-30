/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.mybatis;


import com.sonsure.dumper.core.command.simple.AbstractSimpleCommandExecutor;
import com.sonsure.dumper.core.command.simple.SimpleExecutorContext;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

import java.util.Map;

/**
 * Created by liyd on 17/4/25.
 */
public class MybatisExecutorImpl extends AbstractSimpleCommandExecutor<MybatisExecutor> implements MybatisExecutor {

    protected MybatisExecutorContext mybatisExecutorContext;

    public MybatisExecutorImpl(JdbcEngineConfig jdbcEngineConfig) {
        super(jdbcEngineConfig);
        mybatisExecutorContext = new MybatisExecutorContext();
    }

    @Override
    public MybatisExecutor parameters(Map<String, Object> parameters) {
        if (parameters != null) {
            this.mybatisExecutorContext.addParameters(parameters);
        }
        return this;
    }

    @Override
    public MybatisExecutor parameter(String name, Object value) {
        this.mybatisExecutorContext.addParameter(name, value);
        return this;
    }

    @Override
    protected SimpleExecutorContext getSimpleExecutorContext() {
        return mybatisExecutorContext;
    }
}
