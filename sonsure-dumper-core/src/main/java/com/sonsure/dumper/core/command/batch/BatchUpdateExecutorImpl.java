/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   https://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.batch;

import com.sonsure.dumper.core.command.AbstractCommandExecutor;
import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.CommandType;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

import java.util.Collection;

/**
 * The type Batch update executor.
 *
 * @author liyd
 */
@SuppressWarnings("rawtypes")
public class BatchUpdateExecutorImpl extends AbstractCommandExecutor implements BatchUpdateExecutor {

    private BatchUpdateExecutorContext batchUpdateExecutorContext;

    public BatchUpdateExecutorImpl(JdbcEngineConfig jdbcEngineConfig) {
        super(jdbcEngineConfig);
        batchUpdateExecutorContext = new BatchUpdateExecutorContext<>();
    }

    @Override
    public BatchUpdateExecutor nativeCommand() {
        batchUpdateExecutorContext.setNativeCommand(true);
        return this;
    }

    @Override
    @SuppressWarnings("unchecked")
    public <T> Object execute(String command, Collection<T> batchData, int batchSize, ParameterizedSetter<T> parameterizedSetter) {
        batchUpdateExecutorContext.setCommand(command);
        batchUpdateExecutorContext.setBatchSize(batchSize);
        batchUpdateExecutorContext.setBatchData(batchData);
        batchUpdateExecutorContext.setParameterizedSetter(parameterizedSetter);
        CommandContext commandContext = this.getCommandContextBuilder().build(batchUpdateExecutorContext, getJdbcEngineConfig());
        return getJdbcEngineConfig().getPersistExecutor().execute(commandContext, CommandType.BATCH_UPDATE);
    }

}
