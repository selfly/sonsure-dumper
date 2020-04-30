/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.test.executor;

import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandContextBuilder;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.config.JdbcEngineConfig;
import com.sonsure.dumper.core.persist.PersistExecutor;

public class CountCommandExecutorImpl implements CountCommandExecutor {

    private JdbcEngineConfig jdbcEngineConfig;

    private CommandContextBuilder commandContextBuilder;

    private CountExecutorContext countExecutorContext;

    public CountCommandExecutorImpl(JdbcEngineConfig jdbcEngineConfig) {
        this.jdbcEngineConfig = jdbcEngineConfig;
        countExecutorContext = new CountExecutorContext();
    }

    @Override
    public CountCommandExecutor clazz(Class<?> clazz) {
        this.countExecutorContext.setClazz(clazz);
        return this;
    }

    @Override
    public long getCount() {
        CommandContext commandContext = this.commandContextBuilder.build(this.countExecutorContext, this.jdbcEngineConfig);
        PersistExecutor persistExecutor = this.jdbcEngineConfig.getPersistExecutor();
        commandContext.setResultType(Long.class);
        Object result = persistExecutor.execute(commandContext, CommandType.QUERY_ONE_COL);
        return (Long) result;
    }

    public void setCommandContextBuilder(CommandContextBuilder commandContextBuilder) {
        this.commandContextBuilder = commandContextBuilder;
    }
}
