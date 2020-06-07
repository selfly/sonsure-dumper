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
import com.sonsure.dumper.core.config.JdbcEngineConfig;

import java.util.Map;

/**
 * @author liyd
 * @date 17/4/25
 */
public class MybatisExecutorImpl extends AbstractSimpleCommandExecutor<MybatisExecutor> implements MybatisExecutor {

    public MybatisExecutorImpl(JdbcEngineConfig jdbcEngineConfig) {
        super(jdbcEngineConfig);
        this.getCommandExecutorContext().setNamedParameter(true);
    }

    @Override
    public MybatisExecutor parameters(Map<String, Object> parameters) {
        if (parameters == null) {
            return this;
        }
        for (Map.Entry<String, Object> entry : parameters.entrySet()) {
            this.getCommandExecutorContext().addCommandParameter(entry.getKey(), entry.getValue());
        }
        return this;
    }

    @Override
    public MybatisExecutor parameter(String name, Object value) {
        this.getCommandExecutorContext().addCommandParameter(name, value);
        return this;
    }
}
