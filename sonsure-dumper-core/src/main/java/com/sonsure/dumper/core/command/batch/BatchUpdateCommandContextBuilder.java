/*
 * Copyright (c) 2020. www.sonsure.com Inc. All rights reserved.
 * You may obtain more information at
 *
 *   http://www.sonsure.com
 *
 * Designed By Selfly Lee (selfly@live.com)
 */

package com.sonsure.dumper.core.command.batch;

import com.sonsure.dumper.core.command.CommandContext;
import com.sonsure.dumper.core.command.ExecutorContext;
import com.sonsure.dumper.core.command.entity.AbstractCommandContextBuilder;
import com.sonsure.dumper.core.config.JdbcEngineConfig;

/**
 * The type Batch update command context builder.
 *
 * @author liyd
 */
public class BatchUpdateCommandContextBuilder extends AbstractCommandContextBuilder {

    @Override
    public CommandContext doBuild(ExecutorContext executorContext, JdbcEngineConfig jdbcEngineConfig) {
        BatchUpdateExecutorContext<?> batchUpdateExecutorContext = (BatchUpdateExecutorContext<?>) executorContext;
        return new BatchCommandContext<>(batchUpdateExecutorContext);
    }
}
